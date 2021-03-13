package ui.controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import json.JSONWriter;
import thumbnail.generate.Thumbnail;
import thumbnail.generate.ThumbnailFromFile;
import ui.factory.alert.AlertFactory;
import tournament.Tournament;
import ui.window.EditTournamentWindow;

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

    private static String tournamentFile = "settings/tournaments/tournaments.json";
    private AlertFactory alertFactory = new AlertFactory();

    private static Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            alertFactory.displayWarning("2 fighters must be chosen before generating the thumbnail.");
            return;
        }

        if (tournamentsController.getSelectedTournament() == null){
            System.out.println("Tournament was not chosen");
            alertFactory.displayWarning("A tournament must be chosen before generating the thumbnail.");
            return;
        }

        try {
            Thumbnail.generateAndSaveThumbnail(tournamentsController.getSelectedTournament(), saveLocally.isSelected(), round.getText().toUpperCase(), date.getText(),
                    player1Controller.generateFighter(),
                    player2Controller.generateFighter());
            alertFactory.displayInfo("Thumbnail was successfully generated and saved!");
        }catch (LocalImageNotFoundException e){
            alertFactory.displayError(e.getMessage());
        }catch (OnlineImageNotFoundException e){
            alertFactory.displayError(e.getMessage());
        }catch (FontNotFoundException e){
            alertFactory.displayError(e.getMessage());
        }
    }

    public void createMultipleThumbnails(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            ThumbnailFromFile tbf = new ThumbnailFromFile();
            try {
                tbf.generateFromFile(selectedFile, saveLocally.isSelected());
                alertFactory.displayInfo("Thumbnails were successfully generated and saved!");
            }catch(ThumbnailFromFileException e){
                //alertFactory already thrown inside tbf.generateFromFile
            }catch(FontNotFoundException e){
                alertFactory.displayError(e.getMessage());
            }
        }
    }

    //MenuBar
    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void openEditTournaments(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/fxml/tournamentsSettings.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Tournaments");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static List<Tournament> getTournamentsList() {
        return TournamentsController.getTournamentsList();
    }

    public static void updateTournamentsList(List<Tournament> newTournamentsList) {
        JSONWriter.updateTournamentsFile(newTournamentsList);
        startApp(stage);
    }

    public static void startApp(Stage primaryStage){
        try {
            FXMLLoader loader = new FXMLLoader(ThumbnailGeneratorController.class.getClassLoader().getResource("ui/fxml/thumbnailGenerator.fxml"));
            Parent root = (Parent) loader.load();
            ((ThumbnailGeneratorController) loader.getController()).setStage(primaryStage);

            //primaryStage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
            primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");
            primaryStage.setScene(new Scene(root, 800, 660));
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }


}
