package thumbnailgenerator.ui.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.enums.RivalsOfAether2Enum;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.StreetFighter6Enum;
import thumbnailgenerator.enums.Tekken8Enum;
import thumbnailgenerator.enums.interfaces.FighterArtType;
import thumbnailgenerator.factory.CharacterImageFetcherFactory;
import thumbnailgenerator.ui.combobox.InputFilter;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.enums.StartGGEnumUtils;

@Component
@Scope("prototype")
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
    private @Autowired CharacterImageFetcherFactory characterImageFetcherFactory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFightersComboBox(fighter, StartGGEnumUtils.getAllNames(SmashUltimateEnum.class));
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
        setUrlName(getSelectionName());
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

        switch (parentController.getGame()) {
            case ROA2:
                return StartGGEnumUtils
                        .findCodeByName(RivalsOfAether2Enum.class, sel);
            case SF6:
                return StartGGEnumUtils
                        .findCodeByName(StreetFighter6Enum.class, sel);
            case SSBU:
                return StartGGEnumUtils
                        .findCodeByName(SmashUltimateEnum.class, sel);
            case TEKKEN8:
                return StartGGEnumUtils
                        .findCodeByName(Tekken8Enum.class, sel);
        }
        return null;
    }

    protected void updateFighterIcon(){
        val gameCode = parentController.getGame().getCode();
        try {
            iconLink.setDisable(false);
            icon.setImage(new Image(getClass().getResourceAsStream("/icons/" + gameCode + "/" + urlName + "/" + alt.getValue() + ".png")));
            if (SmashUltimateEnum.PYRA.getName().equals(urlName)){
                icon2Link.setDisable(false);
                Image imageIcon2 = new Image(getClass().getResourceAsStream("/icons/ssbu/mythra/" + alt.getValue() + ".png"));
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
        switch (game) {
            case ROA2:
                initFightersComboBox(fighter, StartGGEnumUtils.getAllNames(RivalsOfAether2Enum.class));
                alt.setDisable(true);
                break;
            case SF6:
                initFightersComboBox(fighter, StartGGEnumUtils.getAllNames(StreetFighter6Enum.class));
                alt.setDisable(true);
                break;
            case SSBU:
                initFightersComboBox(fighter, StartGGEnumUtils.getAllNames(SmashUltimateEnum.class));
                alt.setDisable(false);
                break;
            case TEKKEN8:
                initFightersComboBox(fighter, StartGGEnumUtils.getAllNames(Tekken8Enum.class));
                alt.setDisable(true);
                break;
        }
    }

    public void previewFighter(ActionEvent actionEvent)
            throws MalformedURLException {
        Game game = parentController.getGame();
        FighterArtType artType = parentController.getFighterArtType();
        val imageFetcher = characterImageFetcherFactory.getCharacterImageFetcher(game);
        String url = imageFetcher.getOnlineUrl(generatePlayer().getFighter(0), artType).toString();

        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
        }catch(IOException e ){
            AlertFactory.displayError("Could not open the following URL: "+ url, e.getMessage());
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

    public void setUrlName(String urlName){
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
