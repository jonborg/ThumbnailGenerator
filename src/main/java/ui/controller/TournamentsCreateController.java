package ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import thumbnail.generate.Thumbnail;
import thumbnail.text.TextSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.combobox.InputFilter;
import ui.factory.alert.AlertFactory;
import ui.textfield.ChosenImageField;
import ui.textfield.ChosenJsonField;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class TournamentsCreateController implements Initializable {

    @FXML
    protected TextField name;
    @FXML
    protected TextField id;
    @FXML
    protected ChosenImageField logo;
    @FXML
    protected ChosenImageField foreground;
    @FXML
    protected ChosenImageField background;
    @FXML
    protected ChosenJsonField fighterImageSettingsFile;

    @FXML
    protected ComboBox<String> font;
    @FXML
    protected TextField sizeTop;
    @FXML
    protected TextField angleTop;
    @FXML
    protected TextField sizeBottom;
    @FXML
    protected TextField angleBottom;
    @FXML
    protected CheckBox bold;
    @FXML
    protected CheckBox italic;
    @FXML
    protected CheckBox shadow;
    @FXML
    protected TextField contour;
    @FXML
    protected TextField downOffsetTopLeft;
    @FXML
    protected TextField downOffsetTopRight;
    @FXML
    protected TextField downOffsetBottomLeft;
    @FXML
    protected TextField downOffsetBottomRight;

    @FXML
    protected Button cancelButton;
    @FXML
    protected ImageView preview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNumberTextFields();
        initFontDropdown();
    }

    protected void initNumberTextFields(){
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("-?([0-9]*)?")) {
                return change;
            }
            return null;
        };
        UnaryOperator<TextFormatter.Change> floatFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("-?([0-9\\.]*)?")) {
                return change;
            }
            return null;
        };

        List<TextField> listIntFields = new ArrayList<>(Arrays.asList(
                sizeTop, sizeBottom,
                downOffsetTopLeft, downOffsetTopRight, downOffsetBottomLeft, downOffsetBottomRight)
        );
        List<TextField> listFloatFields = new ArrayList<>(Arrays.asList(
                angleTop, angleBottom, contour)
        );

        for (TextField intField : listIntFields){
            intField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
            intField.focusedProperty().addListener((arg0,newValue, oldValue) -> emptyTextField(intField));
        }
        for (TextField floatField : listFloatFields){
            floatField.setTextFormatter(new TextFormatter<>(new FloatStringConverter(), 0.0f, floatFilter));
            floatField.focusedProperty().addListener((arg0,newValue, oldValue) -> emptyTextField(floatField));
        }
    }

    protected void initFontDropdown(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        String[] allCompatibleFonts =  Stream.concat(Arrays.stream(fontFamilies),
                Arrays.stream(new String[]{"BebasNeue-Regular","Pikmin-Normal"})).sorted()
                .toArray(String[]::new);

        ObservableList<String> items = FXCollections.observableArrayList(allCompatibleFonts);
        FilteredList<String> filteredItems = new FilteredList<>(items);

        font.getEditor().textProperty().addListener(new InputFilter(font, filteredItems, false));
        font.setItems(filteredItems);
    }

    private void emptyTextField(TextField tf){
        if (!tf.isFocused() && tf.getText().isEmpty()){
            tf.setText("0");
        }
    }


    public void previewThumbnail(ActionEvent actionEvent) {
        if (!validateParameters()){
            return;
        }
        Tournament tournament = generateTournamentWithCurrentSettings();

        try{
            BufferedImage previewImage = Thumbnail.generatePreview(tournament);
            Image image = SwingFXUtils.toFXImage(previewImage, null);
            preview.setImage(image);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    protected Tournament generateTournamentWithCurrentSettings(){
        Tournament tournament =  new Tournament(id.getText(), name.getText(),
                logo.getText(), foreground.getText(), background.getText(), fighterImageSettingsFile.getText());
        TextSettings textSettings = new TextSettings(id.getText(), font.getSelectionModel().getSelectedItem(),
                bold.isSelected(), italic.isSelected(), shadow.isSelected(), Float.parseFloat(contour.getText()),
                Integer.parseInt(sizeTop.getText()), Integer.parseInt(sizeBottom.getText()),
                Float.parseFloat(angleTop.getText()), Float.parseFloat(angleBottom.getText()),
                new int[]{Integer.parseInt(downOffsetTopLeft.getText()), Integer.parseInt(downOffsetTopRight.getText())},
                new int[]{Integer.parseInt(downOffsetBottomLeft.getText()), Integer.parseInt(downOffsetBottomRight.getText())});
        tournament.setTextSettings(textSettings);

        return tournament;
    }


    protected boolean validateParameters(){
        String missingParameters = missingParameters();
        if (!missingParameters.isEmpty()){
            AlertFactory.displayError("There are missing obligatory parameters:", missingParameters);
            return false;
        }
        return true;
    }

    private String missingParameters(){
        StringBuffer missingParameters = new StringBuffer();

        if (name.getText().isEmpty()){
            missingParameters.append("Tournament Name");
        }
        if (id.getText().isEmpty()){
            if (!missingParameters.toString().isEmpty()){
                missingParameters.append(", ");
            }
            missingParameters.append("Tournament Diminutive");
        }
        if (font.getSelectionModel().getSelectedItem().isEmpty()){
            if (!missingParameters.toString().isEmpty()){
                missingParameters.append(", ");
            }
            missingParameters.append("Font");
        }
        if (foreground.getText().isEmpty()){
            if (!missingParameters.toString().isEmpty()){
                missingParameters.append(", ");
            }
            missingParameters.append("Foreground");
        }
        if (background.getText().isEmpty()){
            if (!missingParameters.toString().isEmpty()){
                missingParameters.append(", ");
            }
            missingParameters.append("Background");
        }

        if (!missingParameters.toString().isEmpty()){
            missingParameters.append(System.lineSeparator());
        }
        return missingParameters.toString();
    }



    public void save(ActionEvent actionEvent) {
        if (!validateParameters()){
            return;
        }
        Tournament currentTournament = generateTournamentWithCurrentSettings();
        updateTournamentsListAndSettings(currentTournament);
        cancel(actionEvent);
    }

    public void cancel(ActionEvent actionEvent)  {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    protected static void updateTournamentsListAndSettings(Tournament... list){
        TournamentUtils.updateTournamentsListAndSettings(list);
    }
}
