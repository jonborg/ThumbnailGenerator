package ui.controller;

import fighter.DownloadFighterURL;
import fighter.Fighter;
import fighter.Names;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ui.combobox.InputFilter;
import ui.factory.alert.AlertFactory;

public class PlayerController implements Initializable {
    @FXML
    protected TextField player;
    @FXML
    protected ComboBox<String> fighter;
    @FXML
    protected CheckBox flip;
    @FXML
    protected Spinner<Integer> alt;
    @FXML
    protected Hyperlink iconLink;
    @FXML
    protected ImageView icon;
    @FXML
    protected Hyperlink icon2Link;
    @FXML
    protected ImageView icon2;
    @FXML
    protected HBox colorBox;

    protected String urlName;

    private ThumbnailGeneratorController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFighters();
        initAlts();
    }

    protected void initFighters(){
        ObservableList<String> items = FXCollections.observableArrayList(Names.getKeySet());
        FilteredList<String> filteredItems = new FilteredList<>(items);

        fighter.getEditor().textProperty().addListener(new InputFilter(fighter, filteredItems, false));
        fighter.setItems(filteredItems);
    }

    protected void initAlts(){
        alt.valueProperty().addListener(((observable, oldValue, newValue) -> updateFighterIcon()));
    }


    public void selectFighter(ActionEvent actionEvent) {
        urlName = getSelectionName();
        updateFighterIcon();
    }

    protected String getSelectionName() {
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
        return sel == null ? null : Names.getValue(sel);
    }

    protected void updateFighterIcon(){
        try {
            iconLink.setDisable(false);
            icon.setImage(new Image(getClass().getResourceAsStream("/icons/" + urlName + "/" + alt.getValue() + ".png")));
            if ("pyra".equals(urlName)){
                icon2Link.setDisable(false);
                Image imageIcon2 = new Image(getClass().getResourceAsStream("/icons/mythra/" + alt.getValue() + ".png"));
                icon2.setImage(imageIcon2);
                colorBox.setSpacing(0);
            }else{
                icon2Link.setDisable(true);
                icon2.setImage(null);
                colorBox.setSpacing(30);
            }
        }catch (NullPointerException e){
            iconLink.setDisable(true);
            iconLink.setText(null);
            icon.setImage(null);
        }
    }

    public void previewFighter(ActionEvent actionEvent) {
        String url = DownloadFighterURL.generateFighterURL(urlName, alt.getValue(), parentController.getFighterArtType());
        if(Desktop.isDesktopSupported()) {
            try {
                if ("http".equals(url.substring(0,4))){
                    Desktop.getDesktop().browse(new URI(url));
                } else {
                    Desktop.getDesktop().open(new File(url));
                }
            }catch(URISyntaxException | IOException e ){
                AlertFactory.displayError("Could not open the following URL: "+ url, e.getMessage());
            }
        }
    }

    public Fighter generateFighter(){
        return new Fighter(getPlayer(), getFighter() ,getUrlName(), getAlt(), isFlip());
    }

    public String toString(){
        return "Name: " + this.player.getText() +
                " | Character: " + this.fighter.getSelectionModel().getSelectedItem() +
                " | Alt: " + this.alt.getValue();
    }

    public String getPlayer(){
        return this.player.getText();
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

    public void setParentController(ThumbnailGeneratorController parentController) {
        this.parentController = parentController;
    }

}
