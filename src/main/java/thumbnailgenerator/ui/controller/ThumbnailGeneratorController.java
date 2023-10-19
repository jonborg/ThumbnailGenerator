package thumbnailgenerator.ui.controller;

import com.google.gson.reflect.TypeToken;
import exception.Top8FromFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import thumbnailgenerator.JavaFxApplication;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.exception.FontNotFoundException;
import thumbnailgenerator.exception.LocalImageNotFoundException;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.exception.ThumbnailFromFileException;
import thumbnailgenerator.factory.CharacterImageFetcherFactory;
import thumbnailgenerator.service.StartGGService;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.service.ThumbnailService;
import thumbnailgenerator.service.Top8Service;
import thumbnailgenerator.utils.converter.SmashUltimateFighterArtTypeConverter;
import thumbnailgenerator.service.TournamentUtils;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.converter.GameConverter;
import thumbnailgenerator.utils.json.JSONReader;

@Controller
public class ThumbnailGeneratorController implements Initializable {

    private final Logger LOGGER = LogManager.getLogger(ThumbnailGeneratorController.class);
    private static Stage stage;
    private @FXML AnchorPane tournaments;
    private @FXML TournamentsController tournamentsController;
    private @FXML TextField round;
    private @FXML TextField date;
    private @FXML ComboBox<SmashUltimateFighterArtType> artTypeComboBox;
    private @FXML ComboBox<Game> gameComboBox;
    private @FXML AnchorPane player1;
    private @FXML PlayerController player1Controller;
    private @FXML AnchorPane player2;
    private @FXML PlayerController player2Controller;
    private @FXML Button flipPlayer;
    private @FXML CheckBox saveLocally;
    private @FXML Button saveButton;
    private @FXML Button fromFile;
    private @FXML Menu menuCopy;
    private @FXML Menu menuEdit;
    private @FXML Menu menuDelete;
    private @Autowired ThumbnailService thumbnailService;
    private @Autowired Top8Service top8Service;
    private @Autowired CharacterImageFetcherFactory characterImageFetcherFactory;
    private @Autowired StartGGService startGGService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player1Controller.setParentController(this);
        player2Controller.setParentController(this);
        initMenuBars();
        initArtDropdown();
    }

    public void flipPlayers(ActionEvent actionEvent) {
        LOGGER.info("User clicked on flip players button.");
        LOGGER.debug("Player 1 -> {} is now Player 2", player1Controller.toString());
        LOGGER.debug("Player 2 -> {} is now Player 1", player2Controller.toString());
        String nameAux = player1Controller.getPlayer();
        player1Controller.setPlayer(player2Controller.getPlayer());
        player2Controller.setPlayer(nameAux);

        int auxAlt1 = 1;
        int auxAlt2 = 1;

        if (player1Controller.getUrlName() != null)    {
            auxAlt1 = player1Controller.getAlt();
        }
        if (player2Controller.getUrlName() != null) {
            auxAlt2 = player2Controller.getAlt();
        }

        String auxSel = player1Controller.getFighter();
        player1Controller.setFighter(player2Controller.getFighter());
        player2Controller.setFighter(auxSel);

        player1Controller.setAlt(auxAlt2);
        player2Controller.setAlt(auxAlt1);
    }

    public void createThumbnail(ActionEvent actionEvent) {
        LOGGER.info("Creating a single thumbnail.");
        if (player1Controller.getUrlName() == null || player2Controller.getUrlName() == null){
            LOGGER.error("User did not select characters for all players.");
            AlertFactory.displayWarning("It is required to select a character for all players before generating the thumbnail.");
            return;
        }

        if (getSelectedTournament() == null){
            LOGGER.error("User did not select a tournament");
            AlertFactory.displayWarning("A tournament must be chosen before generating the thumbnail.");
            return;
        }

        LOGGER.info("Loading image settings of tournament {} for game {}", getSelectedTournament().getName(), getGame());
        var imageSettings = (ImageSettings) JSONReader.getJSONArrayFromFile(
                getSelectedTournament().getThumbnailSettingsByGame(getGame())
                        .getFighterImageSettingsFile(getFighterArtType()),
                new TypeToken<ArrayList<ImageSettings>>() {}.getType())
                .get(0);

        try {
            thumbnailService.generateAndSaveThumbnail(Thumbnail.builder()
                                                                .tournament(getSelectedTournament())
                                                                .game(getGame())
                                                                .imageSettings(imageSettings)
                                                                .locally(saveLocally.isSelected())
                                                                .round(round.getText().toUpperCase())
                                                                .date(date.getText())
                                                                .players(
                                                                        Thumbnail
                                                                                .
                                                                        createPlayerList(
                                                                                player1Controller.generatePlayer(),
                                                                                player2Controller.generatePlayer()))
                                                                .artType(getFighterArtType())
                                                                .build());
            LOGGER.info("Thumbnail was successfully generated and saved!");
            AlertFactory.displayInfo("Thumbnail was successfully generated and saved!");
        } catch (LocalImageNotFoundException e){
            LOGGER.error("An issue occurred when loading an image. {}", e.getMessage());
            AlertFactory.displayError(e.getMessage());
        } catch (OnlineImageNotFoundException e){
            LOGGER.error("An issue occurred when loading an image online. {}", e.getMessage());
            AlertFactory.displayError(e.getMessage());
        } catch (FontNotFoundException e){
            LOGGER.error("An issue occurred when loading a font. {}", e.getMessage());
            AlertFactory.displayError(e.getMessage());
        } catch (FighterImageSettingsNotFoundException e){
            LOGGER.error("An issue occurred when loading image settings of a character. {}", e.getMessage());
            AlertFactory.displayError(e.getMessage());
        }  catch (MalformedURLException e){
            LOGGER.error("An issue occurred when getting image online. {}", e.getMessage());
            AlertFactory.displayError(e.getMessage());
        }
    }

    //MenuBar
    public void createMultipleThumbnails(ActionEvent actionEvent) {
        LOGGER.info("User chose to generate multiple thumbnails at once.");
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            LOGGER.info("User loaded file {}.", selectedFile.getPath());
            try {
                var inputStream = new FileInputStream(selectedFile);
                thumbnailService
                        .generateAndSaveThumbnailsFromFile(inputStream, saveLocally.isSelected());
                AlertFactory.displayInfo("Thumbnails were successfully generated and saved!");
            }catch(ThumbnailFromFileException e){
                //AlertFactory already thrown inside ThumbnailFromFile.generateFromFile
            }catch(FontNotFoundException | FighterImageSettingsNotFoundException | FileNotFoundException e) {
                AlertFactory.displayError(e.getMessage());
            }
        }
    }

    public void createFromSmashGG(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("thumbnailgenerator/ui/fxml/fromStartGG.fxml"));
            Parent root = loader.load();
            root.setOnMousePressed(e -> root.requestFocus());
            FromStartGGController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Create thumbnails from Start.gg");
            stage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> {
                startGGService.closeClient();
                TournamentUtils.setSelectedTournament(controller.getBackupTournament());});
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(ActionEvent actionEvent) {
        LOGGER.info("Closing application");
        Platform.exit();
    }

    public void createNewTournament(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("thumbnailgenerator/ui/fxml/tournamentsCreate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Create New Tournament");
            stage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initMenuBars(){
        String style = "-fx-padding: 0 100 0 20";
        for (Tournament tournament : getTournamentsList()){
            MenuItem copyOption = new MenuItem(tournament.getName());
            copyOption.setStyle(style);
            copyOption.setOnAction(event -> {
                LOGGER.info("Creating copy of tournament {}.", tournament.toString());
                updateTournamentsList(tournament);
            });
            menuCopy.getItems().add(copyOption);

            MenuItem editOption = new MenuItem(tournament.getName());
            editOption.setStyle(style);
            editOption.setOnAction(event -> {
                setSelectedEdit(tournament);
                try {
                    LOGGER.info("Selected tournament {} for editing.", tournament.getName());
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("thumbnailgenerator/ui/fxml/tournamentsEdit.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Edit Tournament " + tournament.getName());
                    stage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            menuEdit.getItems().add(editOption);

            MenuItem deleteOption = new MenuItem(tournament.getName());
            deleteOption.setStyle(style);
            deleteOption.setOnAction(event -> deleteTournament(tournament));
            menuDelete.getItems().add(deleteOption);

        }
    }

    private void initArtDropdown(){
        artTypeComboBox.getItems().addAll(SmashUltimateFighterArtType.values());
        artTypeComboBox.setConverter(new SmashUltimateFighterArtTypeConverter());
        artTypeComboBox.getSelectionModel().select(SmashUltimateFighterArtType.RENDER);

        gameComboBox.getItems().addAll(Game.values());
        gameComboBox.setConverter(new GameConverter());
        gameComboBox.getSelectionModel().select(Game.SSBU);
        gameComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    player1Controller.updateGameData(newValue);
                    player2Controller.updateGameData(newValue);
                });
    }

    private static List<Tournament> getTournamentsList(){
        return TournamentUtils.getTournamentsList();
    }

    private static Tournament getSelectedTournament() { return TournamentUtils.getSelectedTournament(); }

    private static void setSelectedEdit(Tournament tournament){
        TournamentUtils.setSelectedEdit(tournament);
    }
    private static void updateTournamentsList(Tournament... list) {
        TournamentUtils.updateTournamentsList(list);
    }

    private static void deleteTournament(Tournament tournament) {
        TournamentUtils.deleteTournament(tournament);
    }

    public static void reloadPage(){
        JavaFxApplication.startApp(stage);
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }

    public SmashUltimateFighterArtType getFighterArtType(){
        return artTypeComboBox.getSelectionModel().getSelectedItem();
    }

    public Game getGame(){
        return gameComboBox.getSelectionModel().getSelectedItem();
    }

    public void generateTop8(ActionEvent actionEvent) {
        LOGGER.info("User chose to generate top8.");
        File workingDirectory = new File(System.getProperty("user.dir"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(workingDirectory);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            LOGGER.info("User loaded file {}.", selectedFile.getPath());
            try {
                var inputStream = new FileInputStream(selectedFile);
                top8Service.generateTop8FromFile(inputStream, saveLocally.isSelected());
                AlertFactory.displayInfo("Top 8 was successfully generated and saved!");
            }catch(Top8FromFileException | FileNotFoundException e ){
                //AlertFactory already thrown inside ThumbnailFromFile.generateFromFile
            } catch (FontNotFoundException | FighterImageSettingsNotFoundException e) {
                e.printStackTrace();
        }
    }
}
