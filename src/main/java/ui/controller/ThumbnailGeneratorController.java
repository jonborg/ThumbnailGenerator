package ui.controller;

import app.App;
import com.google.gson.reflect.TypeToken;
import exception.FighterImageSettingsNotFoundException;
import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import exception.ThumbnailFromFileException;
import fighter.FighterArtType;
import fighter.FighterArtTypeConverter;
import file.json.JSONReader;
import java.io.File;
import java.io.IOException;
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
import startgg.query.QueryUtils;
import thumbnail.generate.Thumbnail;
import thumbnail.generate.ThumbnailFromFile;
import thumbnail.generate.ThumbnailSettings;
import thumbnail.image.ImageSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

public class ThumbnailGeneratorController implements Initializable {
    private final Logger LOGGER = LogManager.getLogger(ThumbnailGeneratorController.class);

    @FXML
    private AnchorPane tournaments;
    @FXML
    private TournamentsController tournamentsController;
    @FXML
    private TextField round;
    @FXML
    private TextField date;
    @FXML
    private ComboBox<FighterArtType> artType;
    @FXML
    private AnchorPane player1;
    @FXML
    private PlayerController player1Controller;
    @FXML
    private AnchorPane player2;
    @FXML
    private PlayerController player2Controller;
    @FXML
    private Button flipPlayer;
    @FXML
    private CheckBox saveLocally;
    @FXML
    private Button saveButton;
    @FXML
    private Button fromFile;
    @FXML
    private Menu menuCopy;
    @FXML
    private Menu menuEdit;
    @FXML
    private Menu menuDelete;

    private static Stage stage;

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

        LOGGER.info("Loading image settings of tournament {} ", getSelectedTournament().getName());
        var imageSettings = (ImageSettings) JSONReader.getJSONArray(
                getSelectedTournament().getFighterImageSettingsFile(FighterArtType.RENDER),
                new TypeToken<ArrayList<ImageSettings>>() {}.getType())
                .get(0);

        try {
            Thumbnail.generateAndSaveThumbnail(ThumbnailSettings.builder()
                                                                .tournament(getSelectedTournament())
                                                                .imageSettings(imageSettings)
                                                                .locally(saveLocally.isSelected())
                                                                .round(round.getText().toUpperCase())
                                                                .date(date.getText())
                                                                .fighters(ThumbnailSettings.
                                                                        createFighterList(
                                                                                player1Controller.generateFighter(),
                                                                                player2Controller.generateFighter()))
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
                ThumbnailFromFile.generateFromFile(selectedFile, saveLocally.isSelected());
                AlertFactory.displayInfo("Thumbnails were successfully generated and saved!");
            }catch(ThumbnailFromFileException e){
                //AlertFactory already thrown inside ThumbnailFromFile.generateFromFile
            }catch(FontNotFoundException e){
                AlertFactory.displayError(e.getMessage());
            }
        }
    }

    public void createFromSmashGG(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/fxml/fromStartGG.fxml"));
            Parent root = loader.load();
            root.setOnMousePressed(e -> root.requestFocus());
            FromStartGGController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Create thumbnails from Start.gg");
            stage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> {
                QueryUtils.closeClient();
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
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/fxml/tournamentsCreate.fxml"));
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
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/fxml/tournamentsEdit.fxml"));
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
        artType.getItems().addAll(FighterArtType.values());
        artType.setConverter(new FighterArtTypeConverter());
        artType.getSelectionModel().select(FighterArtType.RENDER);
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
        App.startApp(stage);
    }
    public void setStage(Stage stage){
        this.stage=stage;
    }

    public FighterArtType getFighterArtType(){
        return artType.getSelectionModel().getSelectedItem();
    }
}
