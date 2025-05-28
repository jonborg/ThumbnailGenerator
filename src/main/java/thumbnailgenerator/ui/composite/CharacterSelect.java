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

        initCharacterComboBox(characterList);
        initFighterAltsSpinner();
        initIcons();
    }

    public void setElements(GridPane gridPane, int row, HBox hBox){
        if (row == 1) {
            gridPane.add(characterComboBox, 0, row);
            GridPane.setColumnSpan(characterComboBox, 2);
        } else {
            gridPane.add(characterComboBox, 1, row);
        }
        GridPane.setHalignment(characterComboBox, HPos.RIGHT);

        gridPane.add(altSpinner, 2, row);
        gridPane.add(flipCheckBox, 3, row);
        GridPane.setHalignment(flipCheckBox, HPos.CENTER);
        GridPane.setValignment(flipCheckBox, VPos.CENTER);

        hBox.getChildren().add(iconLink);

        characterComboBox.setId("character"+row);
        altSpinner.setId("alt"+row);
        flipCheckBox.setId("flip"+row);
    }

    public void setStyles(int row){
        if (row == 1) {
            this.characterComboBox.setStyle("{-fx-max-width: 140; -fx-min-width: 140; -fx-pref-width: 140;}");
        } else {
            this.characterComboBox.setStyle("{-fx-max-width: 108; -fx-min-width: 108; -fx-pref-width: 108;}");
        }
        this.altSpinner.setStyle("{-fx-max-width: 50; -fx-min-width: 50; -fx-pref-width: 50;}");
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

    public String getCharacterName(){
        return this.characterComboBox.getSelectionModel().getSelectedItem();
    }

    public void setCharacterName(String name){
        this.characterComboBox.getSelectionModel().select(name);
    }

    public int getAlt(){
        return this.altSpinner.getValue();
    }

    public void setAlt(int i){
        this.altSpinner.getValueFactory().setValue(i);
    }

    public boolean isFlip(){
        return this.flipCheckBox.isSelected();
    }

    public void setFlip(boolean isFlip){
        this.flipCheckBox.setSelected(isFlip);
    }
}
