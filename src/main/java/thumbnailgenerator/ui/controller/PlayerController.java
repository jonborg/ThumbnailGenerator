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
import javafx.scene.control.Button;
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
    private Button addRemoveCharacter;

    protected String urlName;
    protected List<CharacterSelect> characterSelectList;
    private ThumbnailGeneratorController parentController;
    private @Autowired GameEnumService gameEnumService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCharacterBox();
    }

    protected void initCharacterBox(){
        characterSelectList = new ArrayList<>();
        createCharacterSelect(gameEnumService.getAllCharacterNames(Game.SSBU), 1);

        addRemoveCharacter = new Button();
        addRemoveCharacter.setStyle("{-fx-max-width: 25; -fx-min-width: 25; -fx-pref-width: 25;}");
        characterGrid.add(addRemoveCharacter, 0, 2);
        addRemoveCharacter.setText("+");
        addRemoveCharacter.setOnAction(event -> {
            var game = parentController.getGame();
            var chList = gameEnumService.getAllCharacterNames(game);
            if (characterSelectList.size() < 2) {
                createCharacterSelect(chList, 2);
                addRemoveCharacter.setText("-");
            } else {
                removeCharacterSelect(2);
                addRemoveCharacter.setText("+");
            }
        });
    }

    private void createCharacterSelect(List<String> characterList, int row){
        var characterSelect = new CharacterSelect(characterList);
        characterSelect.setStyles(row);
        characterSelect.setElements(characterGrid, row, iconBox);
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

    private void removeCharacterSelect(int row){
        var characterSelect = characterSelectList.get(row-1);
        characterGrid.getChildren().remove(characterSelect.getCharacterComboBox());
        characterGrid.getChildren().remove(characterSelect.getAltSpinner());
        characterGrid.getChildren().remove(characterSelect.getFlipCheckBox());
        iconBox.getChildren().remove(characterSelect.getIcon());
        iconBox.getChildren().remove(characterSelect.getIconLink());

        characterSelectList.remove(row-1);
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

    public List<CharacterSelect> getCharacterSelectList(){
        return characterSelectList;
    }

    public void updateCharacterSelectList(List<CharacterSelect> characterSelectList){
        if (this.characterSelectList.size() != characterSelectList.size()) {
            addRemoveCharacter.fire();
        }
        for (int i = 0; i < this.characterSelectList.size(); i++) {
            var cs = this.characterSelectList.get(i);
            var newCs = characterSelectList.get(i);
            cs.setCharacterName(newCs.getCharacterName());
            cs.setAlt(newCs.getAlt());
            cs.setFlip(newCs.isFlip());
        }
    }

    public void setParentController(ThumbnailGeneratorController parentController) {
        this.parentController = parentController;
    }

    public boolean hasMandatoryFields(){
        var characterSelect = characterSelectList.get(0);
        return characterSelect != null && characterSelect.getCharacterName() != null;
    }

}
