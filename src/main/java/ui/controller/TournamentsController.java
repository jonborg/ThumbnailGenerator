package ui.controller;

import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import json.JSONReader;
import thumbnail.text.TextSettings;
import tournament.Tournament;
import ui.buttons.TournamentButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TournamentsController implements Initializable {

    @FXML
    private  Label tournamentsLabel;
    @FXML
    private HBox tournamentsBox;

    private static List<Tournament> tournamentsList;
    private static String tournamentsFile = "settings/tournaments/tournaments.json";
    private static Tournament selectedTournament;
    private static ToggleGroup tournamentsGroup;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTournamentList();
        initListeners();
    }

    private void initTournamentList(){
        List<TournamentButton> tournamentsButtons = new ArrayList<>();
        tournamentsGroup = new ToggleGroup();
        tournamentsList = loadTournamentsList();
        if (tournamentsList == null || tournamentsList.isEmpty()){
            return;
        }
        tournamentsList.forEach(tournament -> {
            tournament.setTextSettings(TextSettings.loadTextSettings(tournament.getTournamentId()));
            TournamentButton button = new TournamentButton(tournament);
            button.setToggleGroup(tournamentsGroup);
            tournamentsButtons.add(button);
            tournamentsBox.getChildren().add(button);
        });
    }

    private void initListeners(){
        String tournamentsLabelTitle = "Tournament:";
        tournamentsGroup.selectedToggleProperty().addListener((obs,oldToggle,newToggle)->{
            if (newToggle == null){
                tournamentsLabel.setText(tournamentsLabelTitle);
                selectedTournament = null;
            }else{
                for(Node node :  tournamentsBox.getChildren()){
                    if (node instanceof TournamentButton) {
                        TournamentButton tournamentButton = (TournamentButton) node;
                        if (tournamentButton.isSelected()) {
                            selectedTournament = tournamentButton.getTournament();
                            tournamentsLabel.setText(tournamentsLabelTitle + " " + tournamentButton.getName());
                        }
                    }
                }
            }
        });
    }

    public static Tournament getSelectedTournament() {
        return selectedTournament;
    }
    public static List<Tournament> loadTournamentsList() {
        return JSONReader.getJSONArray(tournamentsFile, new TypeToken<ArrayList<Tournament>>(){}.getType());
    }

    public static List<Tournament> getTournamentsList() {
        return tournamentsList;
    }
}
