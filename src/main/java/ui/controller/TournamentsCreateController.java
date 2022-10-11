package ui.controller;

import fighter.image.settings.FighterArtSettings;
import fighter.FighterArtType;
import fighter.FighterArtTypeConverter;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnail.generate.Thumbnail;
import thumbnail.text.TextSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.combobox.InputFilter;
import ui.factory.alert.AlertFactory;
import ui.textfield.ChosenImageField;
import ui.textfield.ChosenJsonField;

public class TournamentsCreateController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(TournamentsCreateController.class);

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
    protected ComboBox<FighterArtType> artType;
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
    protected Button saveButton;
    @FXML
    protected Button cancelButton;
    @FXML
    protected ImageView preview;

    protected List<FighterArtSettings> artTypeDir = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Creating new tournament.");
        initNumberTextFields();
        initFontDropdown();
        initFighterArtTypeDropdown(null);
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

    protected void initFighterArtTypeDropdown(String deprecatedRenderSettings){
        for (var v: FighterArtType.values()) {
            var settingsFile = FighterArtSettings.builder()
                    .artType(v)
                    .build();
            if(deprecatedRenderSettings != null
                    && v.equals(FighterArtType.RENDER)){
                settingsFile.setFighterImageSettingsPath(deprecatedRenderSettings);
            }
            artTypeDir.add(settingsFile);
        }

        artType.getItems().addAll(FighterArtType.values());
        artType.setConverter(new FighterArtTypeConverter());
        artType.getSelectionModel().select(FighterArtType.RENDER);
        artType.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
                    for (var dir : artTypeDir){
                        if (oldValue.equals(dir.getArtType())){
                            dir.setFighterImageSettingsPath(
                                    fighterImageSettingsFile.getText());
                        }
                    }
                    for (var dir : artTypeDir){
                        if (newValue.equals(dir.getArtType())){
                            fighterImageSettingsFile.setText(dir.getFighterImageSettingsPath());
                        }
                    }
                });
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
            BufferedImage previewImage = Thumbnail.generatePreview(tournament, artType.getSelectionModel().getSelectedItem());
            Image image = SwingFXUtils.toFXImage(previewImage, null);
            preview.setImage(image);
        }catch(Exception e){
            LOGGER.catching(e);
        }
    }


    protected Tournament generateTournamentWithCurrentSettings(){
        var lastSelectedArt = artType.getSelectionModel().getSelectedItem();
        for (var dir : artTypeDir){
            if(lastSelectedArt.equals(dir.getArtType())){
                dir.setFighterImageSettingsPath(fighterImageSettingsFile.getText());
            }
        }
        Tournament tournament =  new Tournament(id.getText(), name.getText(),
                logo.getText(), foreground.getText(), background.getText(), artTypeDir);
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
            LOGGER.error("There are missing obligatory parameters.");
            AlertFactory.displayError("There are missing obligatory parameters:", missingParameters);
            return false;
        }
        return true;
    }

    private String missingParameters(){
        StringBuffer missingParameters = new StringBuffer();

        if (name.getText().isEmpty()){
            LOGGER.error("Missing parameter -> Tournament Name");
            missingParameters.append("Tournament Name");
        }
        if (id.getText().isEmpty()){
            LOGGER.error("Missing parameter -> Tournament Diminutive");
            if (!missingParameters.toString().isEmpty()){
                missingParameters.append(", ");
            }
            missingParameters.append("Tournament Diminutive");
        }
        if (font.getSelectionModel().getSelectedItem() == null ||
                font.getSelectionModel().getSelectedItem().isEmpty()){
            LOGGER.error("Missing parameter -> Font");
            if (!missingParameters.toString().isEmpty()){
                missingParameters.append(", ");
            }
            missingParameters.append("Font");
        }
        if (foreground.getText().isEmpty()){
            LOGGER.error("Missing parameter -> Foreground");
            if (!missingParameters.toString().isEmpty()){
                missingParameters.append(", ");
            }
            missingParameters.append("Foreground");
        }
        if (background.getText().isEmpty()){
            LOGGER.error("Missing parameter -> Background");
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
        LOGGER.info("Saving new tournament.");
        if (!validateParameters()){
            return;
        }
        Tournament currentTournament = generateTournamentWithCurrentSettings();
        LOGGER.debug("Saving new tournament -> {}", currentTournament.toString());
        updateTournamentsListAndSettings(currentTournament);
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void cancel(ActionEvent actionEvent)  {
        LOGGER.info("User cancelled tournament creation. Tournament will not be saved.");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    protected static void updateTournamentsListAndSettings(Tournament... list){
        TournamentUtils.updateTournamentsListAndSettings(list);
    }
}
