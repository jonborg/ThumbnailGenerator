package thumbnailgenerator.ui.composite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.ui.combobox.InputFilter;

import java.util.List;

@Getter
public class CharacterSelect {
    private ComboBox<String> characterComboBox;
    private Spinner<Integer> altSpinner;
    private CheckBox flipCheckBox;
    private ImageView icon;
    private Hyperlink iconLink;

    public CharacterSelect(List<String> characterList){
        this.characterComboBox = new ComboBox<>();
        this.altSpinner = new Spinner<>();
        this.flipCheckBox = new CheckBox();
        this.icon = new ImageView();
        this.iconLink = new Hyperlink();

        setStyles();
        initCharacterComboBox(characterList);
        initFighterAltsSpinner();
        initIcons();
    }

    public void setElements(GridPane gridPane, int row, HBox hBox){
        gridPane.add(characterComboBox, 0, row);
        gridPane.add(altSpinner, 1, row);
        gridPane.add(flipCheckBox, 2, row);
        GridPane.setHalignment(flipCheckBox, HPos.CENTER);
        GridPane.setValignment(flipCheckBox, VPos.CENTER);

        hBox.getChildren().add(iconLink);
    }

    private void setStyles(){
        this.characterComboBox.setStyle("{-fx-max-width: 150; -fx-min-width: 150; -fx-pref-width: 150;}");
        this.altSpinner.setStyle("{-fx-max-width: 60; -fx-min-width: 60; -fx-pref-width: 60;}");
        this.icon.setStyle("{height: 64;}");
    }

    public void initCharacterComboBox(List<String> characterList){
        ObservableList<String> observableList = FXCollections.observableArrayList(characterList);
        FilteredList<String> filteredItems = new FilteredList<>(observableList);

        characterComboBox.getEditor().textProperty().addListener(new InputFilter(characterComboBox, filteredItems, false));
        characterComboBox.setItems(filteredItems);
        characterComboBox.setEditable(true);
    }

    private void initFighterAltsSpinner(){
        altSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,8));
        altSpinner.setDisable(true);
    }

    private void initIcons(){
        iconLink.setGraphic(icon);
        iconLink.setDisable(true);
    }

    public void updateValues(String charName, int alt, boolean isFlip){
        this.getCharacterComboBox().getSelectionModel().select(charName);
        this.getAltSpinner().getValueFactory().setValue(alt);
        this.getFlipCheckBox().setSelected(isFlip);
    }

    public String getCharacterName(){
        return this.characterComboBox.getSelectionModel().getSelectedItem();
    }

    public int getAlt(){
        return this.getAltSpinner().getValue();
    }

    public void setAlt(int i){
        getAltSpinner().getValueFactory().setValue(i);
    }

    public boolean isFlip(){
        return this.getFlipCheckBox().isSelected();
    }
}
