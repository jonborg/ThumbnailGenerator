package thumbnailgenerator.ui.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;;
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
import lombok.var;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.StreetFighter6Enum;
import thumbnailgenerator.service.DownloadFighterURL;
import thumbnailgenerator.service.SmashUltimateDownloadFighterURL;
import thumbnailgenerator.ui.combobox.InputFilter;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.enums.EnumUtils;

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
        initFightersComboBox(fighter, EnumUtils.getAllNames(SmashUltimateEnum.class));
        initFighterAltsSpinner(alt);
    }

    protected void initFightersComboBox(ComboBox<String> fighterComboBox, List<String> fighterList){
        ObservableList<String> observableList = FXCollections.observableArrayList(fighterList);
        FilteredList<String> filteredItems = new FilteredList<>(observableList);

        fighterComboBox.getEditor().textProperty().addListener(new InputFilter(fighterComboBox, filteredItems, false));
        fighterComboBox.setItems(filteredItems);
    }

    protected void initFighterAltsSpinner(Spinner<Integer> spinner){
        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> updateFighterIcon()));
    }


    public void selectFighter(ActionEvent actionEvent) {
        urlName = getSelectionName();
        updateFighterIcon();
    }

    protected String getSelectionName() {
        String sel = fighter.getSelectionModel().getSelectedItem();
        if (sel == null || sel.equals(SmashUltimateEnum.MII_BRAWLER.getName())
                || sel.equals(SmashUltimateEnum.MII_SWORDFIGHTER.getName())) {
            alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1));
        } else {
            if (sel.equals(SmashUltimateEnum.MII_GUNNER.getName())
                    || sel.equals(SmashUltimateEnum.RANDOM.getName())) {
                alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
            } else {
                alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));
            }
        }

        if (sel == null){
            return null;
        } else if (Game.SF6.equals(parentController.getGame())){
            return EnumUtils.findCodeByName(StreetFighter6Enum.class, sel);
        } else {
            return EnumUtils.findCodeByName(SmashUltimateEnum.class, sel);
        }
    }

    protected void updateFighterIcon(){
        try {
            iconLink.setDisable(false);
            icon.setImage(new Image(getClass().getResourceAsStream("/icons/" + urlName + "/" + alt.getValue() + ".png")));
            if (SmashUltimateEnum.PYRA.getName().equals(urlName)){
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

    protected void clearFighterComboBox(){
        fighter.getItems().clear();
    }

    protected void updateGameData(Game game){
        if (Game.SF6.equals(game)){
            initFightersComboBox(fighter, EnumUtils.getAllNames(StreetFighter6Enum.class));
            alt.setDisable(true);
        }
        if (Game.SMASH_ULTIMATE.equals(game)) {
            initFightersComboBox(fighter, EnumUtils.getAllNames(SmashUltimateEnum.class));
            alt.setDisable(false);
        }
    }

    public void previewFighter(ActionEvent actionEvent) {
        DownloadFighterURL downloadFighterURL = new SmashUltimateDownloadFighterURL();
        String url = downloadFighterURL
                .generateFighterURL(urlName, alt.getValue(), parentController.getFighterArtType());
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

    public Player generatePlayer(){
        var list = new ArrayList<Fighter>();
        list.add(new Fighter(getFighter(), getUrlName(), getAlt(), isFlip()));
        return new Player(getPlayer(), list);
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
