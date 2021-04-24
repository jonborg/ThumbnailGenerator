package ui.controller;

import app.App;
import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import exception.ThumbnailFromFileException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import smashgg.query.QueryUtils;
import thumbnail.generate.Thumbnail;
import thumbnail.generate.ThumbnailFromFile;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;
import tournament.Tournament;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ThumbnailGeneratorController implements Initializable {

    @FXML
    private AnchorPane tournaments;
    @FXML
    private TournamentsController tournamentsController;
    @FXML
    private TextField round;
    @FXML
    private TextField date;
    @FXML
    private AnchorPane player1;
    @FXML
    private Player1Controller player1Controller;
    @FXML
    private AnchorPane player2;
    @FXML
    private Player2Controller player2Controller;
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
        initMenuBars();
    }

    public void flipPlayers(ActionEvent actionEvent) {
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
        if (player1Controller.getUrlName() == null || player2Controller.getUrlName() == null){
            System.out.println("Missing fighters");
            AlertFactory.displayWarning("2 fighters must be chosen before generating the thumbnail.");
            return;
        }

        if (getSelectedTournament() == null){
            System.out.println("Tournament was not chosen");
            AlertFactory.displayWarning("A tournament must be chosen before generating the thumbnail.");
            return;
        }

        try {
            Thumbnail.generateAndSaveThumbnail(getSelectedTournament(), saveLocally.isSelected(), round.getText().toUpperCase(), date.getText(),
                    player1Controller.generateFighter(),
                    player2Controller.generateFighter());
            AlertFactory.displayInfo("Thumbnail was successfully generated and saved!");
        }catch (LocalImageNotFoundException e){
            AlertFactory.displayError(e.getMessage());
        }catch (OnlineImageNotFoundException e){
            AlertFactory.displayError(e.getMessage());
        }catch (FontNotFoundException e){
            AlertFactory.displayError(e.getMessage());
        }
    }

    //MenuBar
    public void createMultipleThumbnails(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                ThumbnailFromFile.generateFromFile(selectedFile, saveLocally.isSelected());
                AlertFactory.displayInfo("Thumbnails were successfully generated and saved!");
            }catch(ThumbnailFromFileException e){
                //AlertFactory already thrown inside tbf.generateFromFile
            }catch(FontNotFoundException e){
                AlertFactory.displayError(e.getMessage());
            }
        }
    }

    public void createFileSmashGG(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/fxml/fileFromSmashGG.fxml"));
            Parent root = loader.load();
            FileFromSmashGGController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Create file from Smash.gg");
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
        Platform.exit();
    }

    public void createNewTournament(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/fxml/tournamentsCreate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Create New Tournament");
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
            copyOption.setOnAction(event -> updateTournamentsList(tournament));
            menuCopy.getItems().add(copyOption);

            MenuItem editOption = new MenuItem(tournament.getName());
            editOption.setStyle(style);
            editOption.setOnAction(event -> {
                setSelectedEdit(tournament);
                try {
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/fxml/tournamentsEdit.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Edit Tournament " + tournament.getName());
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
}
