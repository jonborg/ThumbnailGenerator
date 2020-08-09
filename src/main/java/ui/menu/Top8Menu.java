package ui.menu;

import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import extension.Thumbnail;
import extension.Top8Chart;
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
import ui.placement.PlacementPane;
import ui.player.PlayerPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Top8Menu {

    private Spinner<Integer> participants = new Spinner<>();
    private Spinner<Integer> tournamentNumber = new Spinner<>();
    private TextField date = new TextField();

    private ArrayList<PlacementPane> placements;

    private String template;
    private String leagueId;

    private AlertFactory alertFactory = new AlertFactory();

    private VBox vbox;

    private Tab tab;

    public Top8Menu(Stage primaryStage){
        //Leagues UI
        Pane allLeaguesBox = generateLeaguesButtons();

        //Date and Participants UI
        tournamentNumber.setMaxWidth(80);
        tournamentNumber.setEditable(true);
        tournamentNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 25556));

        participants.setMaxWidth(80);
        participants.setEditable(true);
        participants.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 25556));

        VBox roundBox1 = new VBox(new Label("Tournament Number:"), tournamentNumber);
        VBox roundBox2 = new VBox(new Label("Participants:"), participants);
        VBox dateBox = new VBox(new Label("Date:"), date);
        HBox dateRoundBox = new HBox(roundBox1, roundBox2, dateBox);
        dateRoundBox.setSpacing(20);
        dateRoundBox.setAlignment(Pos.CENTER);


        //Placements UI
        placements = buildPlacements();
        VBox placesBox = new VBox();
        placesBox.getChildren().addAll(placements);

        HBox placementsBox = new HBox(placesBox);
        placementsBox.setAlignment(Pos.CENTER);

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
        VBox allInfoBox = new VBox(allLeaguesBox, dateRoundBox, placementsBox);
        allInfoBox.setSpacing(20);

        // Save button logic

        saveButton.setOnAction(actionEvent ->{

            if (template == null){
                System.out.println("League was not chosen");
                alertFactory.displayWarning("A league must be chosen before generating the thumbnail.");
                return;
            }

            for (PlacementPane p : placements) {
                if (p.getUrlName1() == null){
                    System.out.println("Missing fighters");
                    alertFactory.displayWarning("All 8 fighters must be chosen before generating the thumbnail.");
                    return;
                }
            }

            Top8Chart t = new Top8Chart();
            try {
                t.generateChart(leagueId, template, saveLocally.isSelected(), tournamentNumber.getValue(), participants.getValue(), date.getText(), placements);
                alertFactory.displayInfo("Thumbnail was successfully generated and saved!");
            }catch (LocalImageNotFoundException e){
                alertFactory.displayError(e.getMessage());
            }catch (OnlineImageNotFoundException e){
                alertFactory.displayError(e.getMessage());
            }catch (FontNotFoundException e){
                alertFactory.displayError(e.getMessage());
            }
        });

        vbox = new VBox(allInfoBox, saveBox);
        vbox.setSpacing(40);
        vbox.setAlignment(Pos.CENTER);

        tab = new Tab("Top 8 (Beta)");
        tab.setContent(vbox);
        tab.setClosable(false);
    }

    private ArrayList<PlacementPane> buildPlacements() {
        ArrayList<PlacementPane> places = new ArrayList<>();

        places.add(new PlacementPane(1));
        places.add(new PlacementPane(2));
        places.add(new PlacementPane(3));
        places.add(new PlacementPane(4));
        places.add(new PlacementPane(5));
        places.add(new PlacementPane(6));
        places.add(new PlacementPane(7));
        places.add(new PlacementPane(8));

        return places;
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
                template = null;
            }else{
                for(LeagueButton league : leaguesButtons){
                    if (league.isSelected()){
                        template = league.getTemplate();
                        leagueId = league.getLeagueId();
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
