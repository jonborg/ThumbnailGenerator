package ui.menu;

import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import exception.ThumbnailFromFileException;
import extension.Thumbnail;
import extension.ThumbnailFromFile;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.factory.alert.AlertFactory;
import ui.league.LeagueButton;
import ui.player.PlayerPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailMenu {

    private Button fromFile = new Button("Generate from file");

    private TextField round = new TextField();
    private TextField date = new TextField();

    private PlayerPane p1 = new PlayerPane(1);
    private PlayerPane p2 = new PlayerPane(2);

    private String foreground;

    private Button flipPlayer = new Button();

    private AlertFactory alertFactory = new AlertFactory();

    private VBox vbox;

    private Tab tab;

    public ThumbnailMenu(Stage primaryStage){
        //Leagues UI
        Pane allLeaguesBox = generateLeaguesButtons();

        //Date and Round UI
        VBox roundBox = new VBox(new Label("Round:"), round);
        VBox dateBox = new VBox(new Label("Date:"), date);
        HBox dateRoundBox = new HBox(roundBox, dateBox);
        dateRoundBox.setSpacing(20);
        dateRoundBox.setAlignment(Pos.CENTER);


        //Players UI
        flipPlayer.setGraphic(new ImageView(new Image(new File("assets/images/ui/flip.png").toURI().toString())));
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

        vbox = new VBox(allInfoBox, saveBox);
        vbox.setSpacing(40);
        vbox.setAlignment(Pos.CENTER);

        tab = new Tab("Thumbnail");
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


    private Pane generatePlayersPane(){

        HBox allPlayersBox = new HBox(p1, flipPlayer, p2);
        allPlayersBox.setSpacing(50);
        p1.setAlignment(Pos.CENTER);
        p2.setAlignment(Pos.CENTER);
        allPlayersBox.setAlignment(Pos.CENTER);

        return allPlayersBox;
    }
}
