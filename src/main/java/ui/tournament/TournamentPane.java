package ui.tournament;

import com.google.gson.reflect.TypeToken;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import json.JSONReader;

import java.util.ArrayList;
import java.util.List;

public class TournamentPane extends Pane {
    private List<Tournament> tournamentList;
    private String tournamentFile = "settings/tournaments/tournaments.json";
    private static Tournament selectedTournament;

    public TournamentPane(){
        String tournamentLabelTitle = "Tournament:";
        Label tournamentLabel = new Label(tournamentLabelTitle);
        ToggleGroup tournamentsGroup = new ToggleGroup();
        List<TournamentButton> tournamentsButtons = new ArrayList<>();

        int buttonSpacing = 10;

        HBox tournamentsBox = new HBox();
        tournamentsBox.setSpacing(buttonSpacing);
        tournamentsBox.setAlignment(Pos.CENTER);

        tournamentList = JSONReader.getJSONArray(tournamentFile, new TypeToken<ArrayList<Tournament>>(){}.getType());
        if (tournamentList == null || tournamentList.isEmpty()){
            return;
        }
        tournamentList.forEach(tournament -> {
            TournamentButton button = new TournamentButton(tournament);
            button.setToggleGroup(tournamentsGroup);
            tournamentsButtons.add(button);
            tournamentsBox.getChildren().add(button);
        });

        tournamentsGroup.selectedToggleProperty().addListener((obs,oldToggle,newToggle)->{
            if (newToggle == null){
                tournamentLabel.setText(tournamentLabelTitle);
                selectedTournament = null;
            }else{
                for(TournamentButton tournament : tournamentsButtons){
                    if (tournament.isSelected()){
                        selectedTournament = tournament.getTournament();
                        tournamentLabel.setText(tournamentLabelTitle + " " + tournament.getName());
                    }
                }
            }
        });

        double buttonWidth = tournamentsButtons.get(0).getPrefWidth();
        double buttonHeight = tournamentsButtons.get(0).getPrefHeight();

        ScrollPane scrollTournamentsBox = new ScrollPane();;
        scrollTournamentsBox.setPrefWidth(4*buttonWidth + 9*buttonSpacing);
        scrollTournamentsBox.setMaxWidth(4*buttonWidth + 9*buttonSpacing);
        scrollTournamentsBox.setPadding(new Insets(buttonSpacing,buttonSpacing,buttonSpacing,buttonSpacing));
        scrollTournamentsBox.setPrefHeight(buttonHeight + 4*buttonSpacing);

        scrollTournamentsBox.setContent(tournamentsBox);
        scrollTournamentsBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox allTournamentsBox = new VBox(tournamentLabel, scrollTournamentsBox);
        allTournamentsBox.setSpacing(buttonSpacing);
        allTournamentsBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(allTournamentsBox);
    }

    public static Tournament getSelectedTournament() {
        return selectedTournament;
    }
}
