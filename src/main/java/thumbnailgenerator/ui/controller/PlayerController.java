package thumbnailgenerator.ui.controller;

import java.io.IOException;
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
import thumbnailgenerator.service.games.GameEnumService;
import thumbnailgenerator.service.Top8Service;
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
    private List<Button> addRemoveCharacter;

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
        addRemoveCharacter = new ArrayList<>();

        createCharacterSelect(gameEnumService.getAllCharacterNames(Game.SSBU), 1);
    }

    private Button createAddRemoveCharacterButton(int row){
        var button = new Button();
        button.setId("addRemoveCharacter"+row);
        button.setStyle("{-fx-max-width: 25; -fx-min-width: 25; -fx-pref-width: 25;}");
        characterGrid.add(button, 0, row);
        button.setText("+");
        button.setOnAction(event -> {
            var game = parentController.getGame();
            var chList = gameEnumService.getAllCharacterNames(game);
            if (characterSelectList.size() < row) {
                createCharacterSelect(chList, row);
                button.setText("-");
            } else {
                removeCharacterSelect(row);
                removeAddRemoveCharacterButtons(row-1);
                button.setText("+");
            }
        });
        return button;
    }

    private void createCharacterSelect(List<String> characterList, int row){
        if (row < 5) {
            addRemoveCharacter.add(createAddRemoveCharacterButton( row + 1));
        }
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
        val clearList = characterSelectList.subList(row-1, characterSelectList.size());
        for (int i = 0; i < clearList.size(); i++) {
            var characterSelect = clearList.get(i);
            characterGrid.getChildren()
                    .remove(characterSelect.getCharacterComboBox());
            characterGrid.getChildren().remove(characterSelect.getAltSpinner());
            characterGrid.getChildren()
                    .remove(characterSelect.getFlipCheckBox());
            iconBox.getChildren().remove(characterSelect.getIcon());
            iconBox.getChildren().remove(characterSelect.getIconLink());
        }
        characterSelectList = characterSelectList.subList(0, row-1);
    }

    private void removeAddRemoveCharacterButtons(int row){
        val clearList = addRemoveCharacter.subList(row, addRemoveCharacter.size());

        for (int i = 0; i<clearList.size(); i++) {
            var button = clearList.get(i);
            characterGrid.getChildren().remove(button);
        }
        addRemoveCharacter = addRemoveCharacter.subList(0, row);
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
        var alt = characterSelect.getAlt();

        try {
            var path = "/icons/" + gameCode + "/" + urlName + "/" + alt + ".png";
            var defaultPath = "/icons/default/1.png";
            var resource = Top8Service.class.getResourceAsStream(path);
            if (resource == null && urlName != null) {
                resource = Top8Service.class.getResourceAsStream(defaultPath);
            }
            var icon = new Image(resource);

            characterSelect.getIconLink().setDisable(false);
            characterSelect.getIcon().setImage(icon);
            characterSelect.getIcon().setFitWidth(icon.getWidth() * 5 / 8);
            characterSelect.getIcon().setFitHeight(icon.getHeight() * 5 / 8);
            characterSelect.getIcon().setPreserveRatio(true);
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

    public String toString(){
        var charactersStringBuilder = new StringBuilder();
        this.characterSelectList
                .forEach( cs -> charactersStringBuilder
                        .append(" | Character: ")
                        .append(cs.getCharacterName())
                        .append(", Alt: ")
                        .append(cs.getAlt())
                        .append(", Flip: ")
                        .append(cs.isFlip())
                );
        return "Name: " + this.player.getText() + charactersStringBuilder.toString();
    }

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
        while (this.characterSelectList.size() != characterSelectList.size()) {
            if (this.characterSelectList.size() > characterSelectList.size()) {
                addRemoveCharacter.get(characterSelectList.size() - 1).fire();
            } else {
                addRemoveCharacter.get(addRemoveCharacter.size() - 1).fire();
            }
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
        return this.characterSelectList
                .stream()
                .noneMatch(cs -> cs == null || cs.getCharacterName() == null);
    }

}
