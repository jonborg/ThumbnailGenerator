package ui.controller;

import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import exception.ThumbnailFromFileException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import thumbnail.generate.Thumbnail;
import thumbnail.generate.ThumbnailFromFile;
import ui.factory.alert.AlertFactory;
import ui.player.PlayerPane;
import ui.tournament.Tournament;
import ui.tournament.TournamentPane;
import ui.window.EditTournamentWindow;

import java.io.File;
import java.util.List;

public class ThumbnailGeneratorController {
    @FXML
    public TournamentPane tournamentPane;
    @FXML
    public TextField round;
    @FXML
    public TextField date;
    @FXML
    public PlayerPane p1;
    @FXML
    public PlayerPane p2;
    @FXML
    public Button flipPlayer;
    @FXML
    public CheckBox saveLocally;
    @FXML
    public Button saveButton;
    @FXML
    public Button fromFile;

    private static String tournamentFile = "settings/tournaments/tournaments.json";
    private AlertFactory alertFactory = new AlertFactory();
    private List<Tournament> tournaments;

    public void flipPlayers(ActionEvent actionEvent) {
        String nameAux = p1.getPlayer();
        p1.setPlayer(p2.getPlayer());
        p2.setPlayer(nameAux);

        int auxAlt1 = 1;
        int auxAlt2 = 1;

        if (p1.getUrlName() != null)    {
            auxAlt1 = p1.getAlt();
        }
        if (p2.getUrlName() != null) {
            auxAlt2 = p2.getAlt();
        }

        String auxSel = p1.getFighter();
        p1.setFighter(p2.getFighter());
        p2.setFighter(auxSel);

        p1.setAlt(auxAlt2);
        p2.setAlt(auxAlt1);
    }



    public void createThumbnail(ActionEvent actionEvent) {
        if (p1.getUrlName() == null || p2.getUrlName() == null){
            System.out.println("Missing fighters");
            alertFactory.displayWarning("2 fighters must be chosen before generating the thumbnail.");
            return;
        }

        if (tournamentPane.getSelectedTournament() == null){
            System.out.println("Tournament was not chosen");
            alertFactory.displayWarning("A tournament must be chosen before generating the thumbnail.");
            return;
        }

        Thumbnail t = new Thumbnail();
        try {
            t.generateThumbnail(tournamentPane.getSelectedTournament(), saveLocally.isSelected(), round.getText().toUpperCase(), date.getText(),
                    p1.generateFighter(),
                    p2.generateFighter());
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
        new EditTournamentWindow(tournaments);
    }

}
