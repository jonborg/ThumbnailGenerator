package ui.menu;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.factory.alert.AlertFactory;
import ui.league.LeagueButton;
import ui.player.PlayerPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Top8Menu {

    private Spinner<Integer> participants = new Spinner<>();
    private TextField date = new TextField();

    private ArrayList<String> placements = new ArrayList<>();

    private String foreground;

    private AlertFactory alertFactory = new AlertFactory();

    private VBox vbox;

    private Tab tab;

    public Top8Menu(Stage primaryStage){
        //Leagues UI
        Pane allLeaguesBox = generateLeaguesButtons();

        //Date and Participants UI
        participants.setMaxWidth(80);
        participants.setEditable(true);
        participants.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 25556));

        VBox roundBox = new VBox(new Label("Participants:"), participants);
        VBox dateBox = new VBox(new Label("Date:"), date);
        HBox dateRoundBox = new HBox(roundBox, dateBox);
        dateRoundBox.setSpacing(20);
        dateRoundBox.setAlignment(Pos.CENTER);


        //Placements UI


        //Save UI
        CheckBox saveLocally = new CheckBox("Save/Load fighters' image locally");

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(100);
        saveButton.setAlignment(Pos.CENTER);

        HBox buttonsBox = new HBox(saveButton);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox saveBox = new VBox(saveLocally, buttonsBox);
        saveBox.setSpacing(10);
        saveBox.setAlignment(Pos.CENTER);


        // Combining all UIs
        //VBox allInfoBox = new VBox(allLeaguesBox, dateRoundBox, something);
        VBox allInfoBox = new VBox(allLeaguesBox, dateRoundBox);
        allInfoBox.setSpacing(20);

        vbox = new VBox(allInfoBox, saveBox);
        vbox.setSpacing(40);
        vbox.setAlignment(Pos.CENTER);

        tab = new Tab("Top 8");
        tab.setContent(vbox);
        tab.setClosable(false);
    }

    public Tab getTab() {
        return tab;
    }

    private Pane generateLeaguesButtons(){
        Label leagueLabel = new Label("League:");
        File[] leaguesImages = new File("assets/images/leagues/").listFiles();
        ToggleGroup leaguesGroup = new ToggleGroup();
        List<LeagueButton> leaguesButtons = new ArrayList<>();

        HBox leaguesBox = new HBox();
        leaguesBox.setSpacing(10);
        leaguesBox.setAlignment(Pos.CENTER);

        for(File image : leaguesImages){
            LeagueButton league = new LeagueButton(image);
            league.setToggleGroup(leaguesGroup);
            leaguesButtons.add(league);
            leaguesBox.getChildren().add(league);
        }

        leaguesGroup.selectedToggleProperty().addListener((obs,oldToggle,newToggle)->{
            if (newToggle == null){
                foreground = null;
            }else{
                for(LeagueButton league : leaguesButtons){
                    if (league.isSelected()){
                        foreground = league.getForeground();
                        leagueLabel.setText("League: " + league.getLeague());
                    }
                }
            }

        });


        VBox allLeaguesBox = new VBox(leagueLabel,leaguesBox);
        allLeaguesBox.setSpacing(5);
        allLeaguesBox.setAlignment(Pos.CENTER);

        return allLeaguesBox;
    }
}
