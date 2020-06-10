package ui.player;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import fighter.Fighter;

public class PlayerPane extends VBox {

    private Map<String,String> map = Names.map;

    private TextField player = new TextField();
    private ComboBox<String> fighter = new ComboBox<>();
    private Spinner<Integer> alt = new Spinner<>();
    private CheckBox flip = new CheckBox();
    private int playerNumber;

    String urlName;

    private PlayerPane(){
        super();
    }

    public PlayerPane(int playerNumber){
        this();
        this.playerNumber = playerNumber;

        fighter.getItems().setAll(map.keySet());
        alt.setMaxWidth(80);
        alt.setEditable(true);
        alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));


        fighter.setOnAction(actionEvent -> {
            urlName = getSelectionName();
            setDefaultFlip(urlName);
        });


        VBox playerBox = new VBox(new Label("Player "+ playerNumber + ":"), player);
        VBox char1Box = new VBox (new Label("Character:"), fighter);
        VBox alt1Box = new VBox(new Label("Alt nº:"), alt);
        VBox fli1Box = new VBox(new Label ("Flip?"), flip);
        HBox allChar1Box = new HBox(char1Box,alt1Box,fli1Box);
        allChar1Box.setSpacing(5);

        this.getChildren().addAll(playerBox,allChar1Box);
        this.setSpacing(10);

    }

    private String getSelectionName() {
        String sel = fighter.getSelectionModel().getSelectedItem();
        if (sel == null || sel.equals("Mii Brawler") || sel.equals("Mii Swordfighter")) {
            alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1));
        } else {
            if (sel.equals("Mii Gunner")) {
                alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
            } else {
                alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));
            }
        }
        return map.get(sel);
    }

    private void setDefaultFlip (String urlName){
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
                        if (playerNumber == 1) flip.setSelected(Boolean.parseBoolean(words[1]));
                        else flip.setSelected(!Boolean.parseBoolean(words[1]));
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

    }

    public Fighter generateFighter(){
        return new Fighter(getPlayer(), getFighter() ,getUrlName(), getAlt(), isFlip());
    }

    public String getPlayer(){
        return this.player.getText().toUpperCase();
    }

    public void setPlayer(String player){
        this.player.setText(player);
    }

    public String getUrlName(){
        return this.urlName;
    }

    public void setUrlName(){
        this.urlName = urlName;
    }

    public String getFighter(){
        return fighter.getSelectionModel().getSelectedItem();
    }

    public void setFighter(String fighter){
        this.fighter.getSelectionModel().select(fighter);
    }

    public int getAlt(){
        return this.alt.getValue();
    }

    public void setAlt(int alt){
        this.alt.getValueFactory().setValue(alt);
    }

    public boolean isFlip(){
        return this.flip.isSelected();
    }

    public void setFlip(boolean flip){
        this.flip.setSelected(flip);
    }
}
