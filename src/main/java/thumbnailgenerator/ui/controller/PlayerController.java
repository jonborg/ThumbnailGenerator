package thumbnailgenerator.ui.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.service.GameEnumService;
import thumbnailgenerator.ui.composite.CharacterSelect;
import thumbnailgenerator.ui.factory.alert.AlertFactory;

@Component
@Scope("prototype")
public class PlayerController implements Initializable {
    @FXML
    protected TextField player;
    @FXML
    protected Hyperlink icon2Link;
    @FXML
    protected ImageView icon2;
    @FXML
    protected GridPane characterGrid;
    @FXML
    protected HBox iconBox;

    protected String urlName;
    protected List<CharacterSelect> characterSelectList;
    private ThumbnailGeneratorController parentController;
    private @Autowired GameEnumService gameEnumService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCharacterBox(gameEnumService.getAllCharacterNames(Game.SSBU));
    }

    protected void initCharacterBox(List<String> characterList){
        characterSelectList = new ArrayList<>();
        createCharacterSelect(characterList, 1);
        createCharacterSelect(characterList, 2);
    }

    private void createCharacterSelect(List<String> characterList, int row){
        var characterSelect = new CharacterSelect(characterList);
        characterSelect.setElements(characterGrid,row, iconBox);
        characterSelect.getCharacterComboBox()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    updateSpinner(newValue, characterSelect);
                    updateFighterIcon(characterSelect);
                });
        characterSelect.getAltSpinner()
                .valueProperty()
                .addListener(((observable, oldValue, newValue) -> updateFighterIcon(characterSelect)));
        characterSelect.getIconLink().setOnAction(actionEvent -> previewFighter(actionEvent, row-1));
        characterSelectList.add(characterSelect);
    }

    protected void updateSpinner(String sel, CharacterSelect characterSelect) {
        var altSpinner = characterSelect.getAltSpinner();
        int altQuantity = 1;
        try {
            altQuantity = gameEnumService.findCharacterAltQuantityByName(parentController.getGame(), sel);
        } catch (NullPointerException ignored) { }
        altSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, altQuantity));
        altSpinner.setDisable(altQuantity <= 1);
    }

    protected void updateFighterIcon(CharacterSelect characterSelect){
        var game = parentController.getGame();
        var gameCode = game.getCode();
        var characterName = characterSelect.getCharacterComboBox().getSelectionModel().getSelectedItem();
        var urlName = gameEnumService.findCharacterCodeByName(game, characterName);
        var alt = characterSelect.getAltSpinner().getValue();

        try {
            characterSelect.getIconLink().setDisable(false);
            characterSelect.getIcon().setImage(new Image(getClass().getResourceAsStream("/icons/" + gameCode + "/" + urlName + "/" + alt + ".png")));
        }catch (NullPointerException e){
            characterSelect.getIconLink().setDisable(true);
            characterSelect.getIconLink().setText(null);
            characterSelect.getIcon().setImage(null);
        }
    }

    protected void updateGameData(Game game){
        characterSelectList.forEach(cs -> {
            cs.getCharacterComboBox().getSelectionModel().clearSelection();
            cs.initCharacterComboBox(gameEnumService.getAllCharacterNames(game));
            cs.getAltSpinner().setDisable(true);
            cs.setAlt(1);
        });
    }

    public void previewFighter(ActionEvent actionEvent, int characterIndex) {
        Game game = parentController.getGame();
        FighterArtTypeEnum artType = parentController.getFighterArtType();
        val imageFetcher = gameEnumService.getCharacterImageFetcher(game);

        try {
            String url = imageFetcher.getOnlineUrl(generatePlayer().getFighter(characterIndex), artType, false).toString();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
        }catch(IOException e ){
            AlertFactory.displayError("Could not open the preview link.", e.getMessage());
        }
    }

    public Player generatePlayer(){
        var game = parentController.getGame();
        var characterList = characterSelectList.stream()
                .map(cs -> {
                    var charName = cs.getCharacterName();
                    var urlName = gameEnumService.findCharacterCodeByName(game, charName);
                    var alt = cs.getAlt();
                    var isFlip = cs.isFlip();
                    return new Fighter(charName, urlName, alt, isFlip);
                })
                .collect(Collectors.toList());
        return new Player(getPlayer(), characterList);
    }

   /* public String toString(){
        return "Name: " + this.player.getText() +
                " | Character: " + this.fighter.getSelectionModel().getSelectedItem() +
                " | Alt: " + this.alt.getValue();
    }*/

    public String getPlayer(){
        return this.player.getText();
    }

    public void setPlayer(String player){
        this.player.setText(player);
    }

    public void setParentController(ThumbnailGeneratorController parentController) {
        this.parentController = parentController;
    }

    public boolean hasMandatoryFields(){
        var characterSelect = characterSelectList.get(0);
        return characterSelect != null && characterSelect.getCharacterName() != null;
    }

}
