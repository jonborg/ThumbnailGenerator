import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import exception.ThumbnailFromFileException;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import json.JSONProcessor;
import org.json.simple.JSONObject;
import ui.factory.alert.AlertFactory;
import ui.player.PlayerPane;
import ui.tournament.TournamentButton;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private String tournamentFile = "settings/tournaments/tournaments.json";

    private Button fromFile = new Button("Generate from file");
    private TextField round = new TextField();
    private TextField date = new TextField();
    private  PlayerPane p1 = new PlayerPane(1);
    private PlayerPane p2 = new PlayerPane(2);
    private Button flipPlayer = new Button();
    private TournamentButton selectedTournament = null;

    private AlertFactory alertFactory = new AlertFactory();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("/logo/smash_ball.png")));
        primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");

        //Tournaments UI
        Pane allTournamentsBox = generateTournamentsButtons();

        //Date and Round UI
        VBox roundBox = new VBox(new Label("Round:"), round);
        VBox dateBox = new VBox(new Label("Date:"), date);
        HBox dateRoundBox = new HBox(roundBox, dateBox);
        dateRoundBox.setSpacing(20);
        dateRoundBox.setAlignment(Pos.CENTER);

        //Players UI
        flipPlayer.setGraphic(new ImageView(new Image(App.class.getResourceAsStream("/ui/flip.png"))));
        Pane allPlayersBox = generatePlayersPane();

        //Save UI
        CheckBox saveLocally = new CheckBox("Save/Load fighters' image locally");

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(100);
        saveButton.setAlignment(Pos.CENTER);

        HBox buttonsBox = new HBox(saveButton, fromFile);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox saveBox = new VBox(saveLocally, buttonsBox);
        saveBox.setSpacing(10);
        saveBox.setAlignment(Pos.CENTER);


        // Combining all UIs
        VBox allInfoBox = new VBox(allTournamentsBox, dateRoundBox, allPlayersBox);
        allInfoBox.setSpacing(20);

        VBox allContentBox = new VBox(allInfoBox, saveBox);
        allContentBox.setSpacing(20);
        allContentBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().add(allContentBox);
        primaryStage.setScene(new Scene(root, 800, 640));
        primaryStage.show();



        //Buttons' listeners
        //save listener
        saveButton.setOnAction(actionEvent ->{

            if (p1.getUrlName() == null || p2.getUrlName() == null){
                System.out.println("Missing fighters");
                alertFactory.displayWarning("2 fighters must be chosen before generating the thumbnail.");
                return;
            }

            if (selectedTournament == null){
                System.out.println("Tournament was not chosen");
                alertFactory.displayWarning("A tournament must be chosen before generating the thumbnail.");
                return;
            }

            Thumbnail t = new Thumbnail();
            try {
                t.generateThumbnail(selectedTournament, saveLocally.isSelected(), round.getText().toUpperCase(), date.getText(),
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
        });

        //load from file listener
        fromFile.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
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
        });

        //flip player info listener
        flipPlayer.setOnAction(actionEvent ->{
            String nameAux = p1.getPlayer();
            p1.setPlayer(p2.getPlayer());
            p2.setPlayer(nameAux);

            int auxAlt1 = 1;
            int auxAlt2 = 1;

            if (p1.getUrlName() != null) {
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
            
        });
    }


    private Pane generateTournamentsButtons(){

        String tournamentLabelTitle = "Tournament:";
        Label tournamentLabel = new Label(tournamentLabelTitle);
        ToggleGroup tournamentsGroup = new ToggleGroup();
        List<TournamentButton> tournamentsButtons = new ArrayList<>();

        int buttonSpacing = 10;

        HBox tournamentsBox = new HBox();
        tournamentsBox.setSpacing(buttonSpacing);
        tournamentsBox.setAlignment(Pos.CENTER);

        JSONProcessor.getJSONArray(tournamentFile).forEach(tournament -> {
            JSONObject t =  (JSONObject) tournament;
            if (t.containsKey("name")) {
                TournamentButton tournamentButton = new TournamentButton(t);
                tournamentButton.setToggleGroup(tournamentsGroup);
                tournamentsButtons.add(tournamentButton);
                tournamentsBox.getChildren().add(tournamentButton);
            }
        });

        tournamentsGroup.selectedToggleProperty().addListener((obs,oldToggle,newToggle)->{
            if (newToggle == null){
                tournamentLabel.setText(tournamentLabelTitle);
                selectedTournament = null;
            }else{
                for(TournamentButton tournament : tournamentsButtons){
                    if (tournament.isSelected()){
                        selectedTournament = tournament;
                        tournamentLabel.setText(tournamentLabelTitle + " " + tournament.getName());
                    }
                }
            }
        });

        double buttonWidth = tournamentsButtons.get(0).getPrefWidth();
        double buttonHeight = tournamentsButtons.get(0).getPrefHeight();

        ScrollPane scrollTournamentsBox = new ScrollPane();
        scrollTournamentsBox.setPrefWidth(4*buttonWidth + 9*buttonSpacing);
        scrollTournamentsBox.setMaxWidth(4*buttonWidth + 9*buttonSpacing);
        scrollTournamentsBox.setPadding(new Insets(buttonSpacing,buttonSpacing,buttonSpacing,buttonSpacing));
        scrollTournamentsBox.setPrefHeight(buttonHeight + 4*buttonSpacing);

        scrollTournamentsBox.setContent(tournamentsBox);
        //scrollTournamentsBox.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollTournamentsBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox allTournamentsBox = new VBox(tournamentLabel, scrollTournamentsBox);
        allTournamentsBox.setSpacing(buttonSpacing);
        allTournamentsBox.setAlignment(Pos.CENTER);

        return allTournamentsBox;
    }


    private Pane generatePlayersPane(){

        HBox allPlayersBox = new HBox(p1, flipPlayer, p2);
        allPlayersBox.setSpacing(50);
        p1.setAlignment(Pos.CENTER);
        p2.setAlignment(Pos.CENTER);
        allPlayersBox.setAlignment(Pos.CENTER);

        return allPlayersBox;
    }
}
