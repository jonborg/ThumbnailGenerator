
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class App extends Application {

    Button fromFile = new Button("Generate from file");

    TextField round = new TextField();
    TextField date = new TextField();

    TextField player1 = new TextField();
    TextField player2 = new TextField();
    
    ComboBox<String> fighter1 = new ComboBox<>();
    ComboBox<String> fighter2 = new ComboBox<>();

    Spinner<Integer> alt1 = new Spinner();
    Spinner<Integer> alt2 = new Spinner();

    CheckBox flip1 = new CheckBox();
    CheckBox flip2 = new CheckBox();

    String foreground;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.getIcons().add(new Image("https://pbs.twimg.com/profile_images/1132749237945552901/v74uelMr_400x400.png"));
        primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");

        Label leagueLabel = new Label("League:");

        Map<String,String> map = Names.map;

        ToggleGroup leagues = new ToggleGroup();
        ToggleButton throwdown = new ToggleButton();
        ToggleButton invicta = new ToggleButton();
        ToggleButton sop = new ToggleButton();
        throwdown.setToggleGroup(leagues);
        invicta.setToggleGroup(leagues);
        sop.setToggleGroup(leagues);
        throwdown.setGraphic(new ImageView(new Image(new File("resources/images/leagues/throwdown.png").toURI().toString())));
        invicta.setGraphic(new ImageView(new Image(new File("resources/images/leagues/invicta.png").toURI().toString())));
        sop.setGraphic(new ImageView(new Image(new File("resources/images/leagues/sop.png").toURI().toString())));
        leagues.selectedToggleProperty().addListener((obs,oldToggle,newToggle)->{
            if (newToggle == null){
               foreground = null;
            }else{
                if(throwdown.isSelected()) {
                    foreground = "foregroundLx.png";
                    leagueLabel.setText("League: Throwdown Lx");
                }
                if(invicta.isSelected()) {
                    foreground = "foregroundPorto.png";
                    leagueLabel.setText("League: Smash Invicta");
                }
                if(sop.isSelected()) {
                    foreground = "foregroundSop.png";
                    leagueLabel.setText("League: Smash or Pass");
                }
            }
        });

        fighter1.getItems().setAll(map.keySet());
        fighter2.getItems().setAll(map.keySet());


        alt1.setMaxWidth(80);
        alt1.setEditable(true);

        alt2.setMaxWidth(80);
        alt2.setEditable(true);

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(100);
        saveButton.setAlignment(Pos.CENTER);


        HBox leaguesBox = new HBox(throwdown,invicta,sop);
        leaguesBox.setSpacing(10);
        leaguesBox.setAlignment(Pos.CENTER);
        VBox allLeaguesBox = new VBox(leagueLabel,leaguesBox);
        allLeaguesBox.setSpacing(5);
        allLeaguesBox.setAlignment(Pos.CENTER);

        VBox roundBox = new VBox(new Label("Round:"), round);
        VBox dateBox = new VBox(new Label("Date:"), date);


        HBox extraBox = new HBox(roundBox, dateBox);
        extraBox.setSpacing(20);
        extraBox.setAlignment(Pos.CENTER);


        VBox player1Box = new VBox(new Label("Player 1:"),player1);
        VBox char1Box = new VBox (new Label("Character:"),fighter1);
        VBox alt1Box = new VBox(new Label("Alt nº:"),alt1);
        VBox fli1Box = new VBox(new Label ("Flip?"), flip1);
        HBox allChar1Box = new HBox(char1Box,alt1Box,fli1Box);
        allChar1Box.setSpacing(5);
        VBox allPlayer1Box= new VBox(player1Box,allChar1Box);
        allPlayer1Box.setSpacing(10);

        Button flipPlayer = new Button();
        flipPlayer.setGraphic(new ImageView(new Image(new File("resources/images/ui/flip.png").toURI().toString())));


        VBox player2Box = new VBox(new Label("Player 2:"),player2);
        VBox char2Box = new VBox (new Label("Character:"),fighter2);
        VBox alt2Box = new VBox(new Label("Alt nº:"),alt2);
        VBox fli2Box = new VBox(new Label ("Flip?"),flip2);
        HBox allChar2Box = new HBox(char2Box,alt2Box,fli2Box);
        allChar2Box.setSpacing(5);
        VBox allPlayer2Box= new VBox(player2Box,allChar2Box);
        allPlayer2Box.setSpacing(10);


        HBox allPlayersBox = new HBox(allPlayer1Box, flipPlayer, allPlayer2Box);
        allPlayersBox.setSpacing(50);
        allPlayer1Box.setAlignment(Pos.CENTER);
        allPlayer2Box.setAlignment(Pos.CENTER);
        allPlayersBox.setAlignment(Pos.CENTER);


        CheckBox saveLocally = new CheckBox("Save/Load fighters' image locally");

        HBox buttonsBox = new HBox(saveButton, fromFile);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox saveBox = new VBox(saveLocally, buttonsBox);
        saveBox.setSpacing(10);
        saveBox.setAlignment(Pos.CENTER);


        VBox allContentBox = new VBox(allLeaguesBox, extraBox, allPlayersBox);
        allContentBox.setSpacing(20);

        VBox allContentBox2 = new VBox(allContentBox, saveBox);
        allContentBox2.setSpacing(40);
        allContentBox2.setAlignment(Pos.CENTER);




        fighter1.setOnAction(actionEvent -> {
            String sel = fighter1.getSelectionModel().getSelectedItem();
            if (sel==null || sel.equals("Mii Brawler")  || sel.equals("Mii Swordfighter")){
                alt1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,1));
            }else {
                if (sel.equals("Mii Gunner")) {
                    alt1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
                } else {
                    alt1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));
                }
            }

            String urlName = map.get(sel);
            boolean skip = false;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("resources/config/flip.txt"), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (urlName == "falco" && !skip && line.contains(urlName)){
                        skip = true;
                        continue;
                    }
                    if (line.contains(urlName)){
                        String[] words = line.split(" ");
                        if (words.length>1)
                            flip1.setSelected(Boolean.parseBoolean(words[1]));
                        break;
                    }
                }
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("ERROR");
                alert.setContentText("Could not detect flip.txt");
                alert.showAndWait();
            }
        });

        fighter2.setOnAction(actionEvent -> {
            String sel = fighter2.getSelectionModel().getSelectedItem();
            if (sel==null || sel.equals("Mii Brawler")  || sel.equals("Mii Swordfighter")){
                alt2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,1));
            }else {
                if (sel.equals("Mii Gunner")) {
                    alt2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
                } else {
                    alt2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));
                }
            }

            String urlName = map.get(sel);
            boolean skip = false;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("resources/config/flip.txt"), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (urlName == "falco" && !skip && line.contains(urlName)){
                        skip = true;
                        continue;
                    }
                    if (line.contains(urlName)){
                        String[] words = line.split(" ");
                        if (words.length>1)
                            flip2.setSelected(!Boolean.parseBoolean(words[1]));
                        break;
                    }
                }
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("ERROR");
                alert.setContentText("Could not detect flip.txt");
                alert.showAndWait();
            }
        });

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String f1 = fighter1.getSelectionModel().getSelectedItem();
                String f2 = fighter2.getSelectionModel().getSelectedItem();
                if (f1==null || f2==null){
                    System.out.println("Missing fighters");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("2 fighters must be chosen before generating the thumbnail.");

                    alert.showAndWait();
                    return;
                }

                if (foreground == null){
                    System.out.println("League was not chosen");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("A league must be chosen before generating the thumbnail.");

                    alert.showAndWait();
                    return;
                }

                String p1 = player1.getText().toUpperCase();
                String p2 = player2.getText().toUpperCase();

                String char1 = map.get(f1);
                String char2 = map.get(f2);

                Thumbnail t = new Thumbnail();
                t.generateThumbnail(foreground, saveLocally.isSelected(), round.getText().toUpperCase(), date.getText(),
                                        new Fighter(p1, char1, alt1.getValue(), flip1.isSelected()),
                                        new Fighter(p2, char2, alt2.getValue(), flip2.isSelected()));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Success");
                alert.setContentText("Thumbnail was successfully generated and saved!");

                alert.showAndWait();
            }
        });

        fromFile.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                ThumbnailFromFile tbf = new ThumbnailFromFile();
                tbf.generateFromFile(selectedFile, saveLocally.isSelected());
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Success");
            alert.setContentText("Thumbnails were successfully generated and saved!");

            alert.showAndWait();
        });

        flipPlayer.setOnAction(actionEvent ->{
            String nameAux = player1.getText();
            player1.setText(player2.getText());
            player2.setText(nameAux);

            int auxAlt1 = 1;
            int auxAlt2 = 1;

            if (!fighter1.getSelectionModel().isEmpty() && !fighter2.getSelectionModel().isEmpty()) {
                auxAlt1 = alt1.getValue();
                auxAlt2 = alt2.getValue();
            }

            String auxSel = fighter1.getSelectionModel().getSelectedItem();
            fighter1.getSelectionModel().select(fighter2.getSelectionModel().getSelectedItem());
            fighter2.getSelectionModel().select(auxSel);

            alt1.getValueFactory().setValue(auxAlt2);
            alt2.getValueFactory().setValue(auxAlt1);

            boolean auxFlip = flip1.isSelected();
            flip1.setSelected(!flip2.isSelected());
            flip2.setSelected(!auxFlip);
        });

        StackPane root = new StackPane();

        root.getChildren().add(allContentBox2);
  //      root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 800, 500));

        primaryStage.show();


    }
}
