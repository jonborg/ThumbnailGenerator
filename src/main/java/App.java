
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.factory.alert.AlertFactory;
import ui.league.LeagueButton;
import ui.player.PlayerPane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    Button fromFile = new Button("Generate from file");

    TextField round = new TextField();
    TextField date = new TextField();

    PlayerPane p1 = new PlayerPane(1);
    PlayerPane p2 = new PlayerPane(2);

    String foreground;

    Button flipPlayer = new Button();

    AlertFactory alertFactory = new AlertFactory();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.getIcons().add(new Image("https://pbs.twimg.com/profile_images/1132749237945552901/v74uelMr_400x400.png"));
        primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");

        //Leagues UI
        Pane allLeaguesBox = generateLeaguesButtons();

        //Date and Round UI
        VBox roundBox = new VBox(new Label("Round:"), round);
        VBox dateBox = new VBox(new Label("Date:"), date);
        HBox dateRoundBox = new HBox(roundBox, dateBox);
        dateRoundBox.setSpacing(20);
        dateRoundBox.setAlignment(Pos.CENTER);


        //Players UI
        flipPlayer.setGraphic(new ImageView(new Image(new File("resources/images/ui/flip.png").toURI().toString())));
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
        VBox allInfoBox = new VBox(allLeaguesBox, dateRoundBox, allPlayersBox);
        allInfoBox.setSpacing(20);

        VBox allContentBox = new VBox(allInfoBox, saveBox);
        allContentBox.setSpacing(40);
        allContentBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().add(allContentBox);
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();



        //Buttons' listeners
        //save listener
        saveButton.setOnAction(actionEvent ->{

            if (p1.getUrlName() == null || p2.getUrlName() == null){
                System.out.println("Missing fighters");
                alertFactory.displayWarning("2 fighters must be chosen before generating the thumbnail.");
                return;
            }

            if (foreground == null){
                System.out.println("League was not chosen");
                alertFactory.displayWarning("A league must be chosen before generating the thumbnail.");
                return;
            }

            Thumbnail t = new Thumbnail();
            try {
                t.generateThumbnail(foreground, saveLocally.isSelected(), round.getText().toUpperCase(), date.getText(),
                        p1.generateFighter(),
                        p2.generateFighter());
                alertFactory.displayInfo("Thumbnail was successfully generated and saved!");
            }catch (LocalImageNotFoundException e){
                
            }catch (OnlineImageNotFoundException e){
                //error previously dealt. Catch is used to interrupt thumbnail generation when fighters images are missing
            }
        });

        //load from file listener
        fromFile.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                ThumbnailFromFile tbf = new ThumbnailFromFile();
                tbf.generateFromFile(selectedFile, saveLocally.isSelected());
            }
            alertFactory.displayInfo("Thumbnails were successfully generated and saved!");
        });

        //flip player info listener
        flipPlayer.setOnAction(actionEvent ->{
            String nameAux = p1.getPlayer();
            p1.setPlayer(p2.getPlayer());
            p2.setPlayer(nameAux);

            int auxAlt1 = 1;
            int auxAlt2 = 1;

            if (p1.getUrlName() != null && p2.getUrlName() != null) {
                auxAlt1 = p1.getAlt();
                auxAlt2 = p2.getAlt();
            }

            String auxSel = p1.getFighter();
            p1.setFighter(p2.getFighter());
            p2.setFighter(auxSel);

            p1.setAlt(auxAlt2);
            p2.setAlt(auxAlt1);
            
        });
    }


    private Pane generateLeaguesButtons(){
        Label leagueLabel = new Label("League:");
        File[] leaguesImages = new File("resources/images/leagues/").listFiles();
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


    private Pane generatePlayersPane(){

        HBox allPlayersBox = new HBox(p1, flipPlayer, p2);
        allPlayersBox.setSpacing(50);
        p1.setAlignment(Pos.CENTER);
        p2.setAlignment(Pos.CENTER);
        allPlayersBox.setAlignment(Pos.CENTER);

        return allPlayersBox;
    }
}
