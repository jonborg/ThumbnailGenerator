package thumbnailgenerator.ui.controller;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import thumbnailgenerator.dto.FighterArtSettings;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.exception.FontNotFoundException;
import thumbnailgenerator.exception.LocalImageNotFoundException;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.service.ThumbnailService;
import thumbnailgenerator.utils.converter.GameConverter;
import thumbnailgenerator.utils.converter.SmashUltimateFighterArtTypeConverter;
import thumbnailgenerator.service.TournamentUtils;
import thumbnailgenerator.ui.combobox.InputFilter;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.ui.textfield.ChosenImageField;
import thumbnailgenerator.ui.textfield.ChosenJsonField;

@Controller
public class TournamentsCreateController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(TournamentsCreateController.class);
    @FXML
    protected ComboBox<Game> gameComboBox;
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
    protected ComboBox<SmashUltimateFighterArtType> artType;
    @FXML
    protected ChosenJsonField fighterImageSettingsFile;
    @FXML
    protected ChosenImageField foregroundTop8;
    @FXML
    protected ChosenImageField backgroundTop8;
    @FXML
    protected ComboBox<SmashUltimateFighterArtType> artTypeTop8;
    @FXML
    protected ChosenJsonField slotSettingsFileTop8;
    @FXML
    protected ChosenJsonField fighterImageSettingsFileTop8;
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
    protected List<FighterArtSettings> artTypeDirTop8 = new ArrayList<>();
    protected List<FileThumbnailSettings> fileThumbnailSettingsList;
    protected List<FileTop8Settings> fileTop8SettingsList;
    private @Autowired ThumbnailService thumbnailService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Creating new tournament.");
        initGamesDropdown(null);
        initNumberTextFields();
        initFontDropdown();
        initFighterArtTypeDropdown(null);
    }

    private List<FighterArtSettings> initArtType(Game game){
        List<FighterArtSettings> list = new ArrayList<>();
        if (Game.SSBU.equals(game)) {
            for (SmashUltimateFighterArtType artType : SmashUltimateFighterArtType
                    .values()) {
                list.add(FighterArtSettings.builder()
                                            .artType(artType)
                                            .fighterImageSettingsPath("")
                                            .build());
            }
        } else {
            list.add(FighterArtSettings.builder()
                    .artType(SmashUltimateFighterArtType.RENDER)
                    .fighterImageSettingsPath("")
                    .build());
        }
        return list;
    }

    protected void initGamesDropdown(Tournament tournament){
        gameComboBox.getItems().addAll(Game.values());
        gameComboBox.setConverter(new GameConverter());
        gameComboBox.getSelectionModel().select(Game.SSBU);
        fileThumbnailSettingsList = new ArrayList<>();
        fileTop8SettingsList = new ArrayList<>();
        for (Game game: Game.values()) {
            fileThumbnailSettingsList
                    .add(new FileThumbnailSettings(game,
                            "", "", initArtType(game), null));
            fileTop8SettingsList
                    .add(new FileTop8Settings(game,
                            "", "", initArtType(game), null));
        } if (tournament != null) {
            for (FileThumbnailSettings settings : tournament.getThumbnailSettings()){
                var settingToUpdate = fileThumbnailSettingsList.stream().filter(s -> s.getGame().equals(settings.getGame()));
                fileThumbnailSettingsList.remove(settingToUpdate);
                fileThumbnailSettingsList.add(settings.clone());
            }
            for (FileTop8Settings settings : tournament.getTop8Settings()){
                var settingToUpdate = fileTop8SettingsList.stream().filter(s -> s.getGame().equals(settings.getGame()));
                fileTop8SettingsList.remove(settingToUpdate);
                fileTop8SettingsList.add(settings.clone());
            }
        }

        gameComboBox.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
                    cacheGeneratedGraphicSettings(oldValue);
                    loadCachedGeneratedGraphicSettings(newValue);
                });

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

    protected void initFighterArtTypeDropdown(String renderSettings){
        for (var v: SmashUltimateFighterArtType.values()) {
            var settingsFile = FighterArtSettings.builder()
                    .artType(v)
                    .fighterImageSettingsPath("")
                    .build();
            if(renderSettings != null
                    && v.equals(SmashUltimateFighterArtType.RENDER)){
                settingsFile.setFighterImageSettingsPath(renderSettings);
            }
            artTypeDir.add(settingsFile);
        }

        artType.getItems().addAll(SmashUltimateFighterArtType.values());
        artType.setConverter(new SmashUltimateFighterArtTypeConverter());
        artType.getSelectionModel().select(SmashUltimateFighterArtType.RENDER);
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
        for (var v: SmashUltimateFighterArtType.values()) {
            var settingsFile = FighterArtSettings.builder()
                    .artType(v)
                    .fighterImageSettingsPath("")
                    .build();
            artTypeDirTop8.add(settingsFile);
        }

        artTypeTop8.getItems().addAll(SmashUltimateFighterArtType.values());
        artTypeTop8.setConverter(new SmashUltimateFighterArtTypeConverter());
        artTypeTop8.getSelectionModel().select(SmashUltimateFighterArtType.RENDER);
        artTypeTop8.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
                    for (var dir : artTypeDirTop8){
                        if (oldValue.equals(dir.getArtType())){
                            dir.setFighterImageSettingsPath(
                                    fighterImageSettingsFileTop8.getText());
                        }
                    }
                    for (var dir : artTypeDirTop8){
                        if (newValue.equals(dir.getArtType())){
                            fighterImageSettingsFileTop8.setText(dir.getFighterImageSettingsPath());
                        }
                    }
                });
    }

    private void emptyTextField(TextField tf){
        if (!tf.isFocused() && tf.getText().isEmpty()){
            tf.setText("0");
        }
    }


    public void previewThumbnail(ActionEvent actionEvent)
            throws FighterImageSettingsNotFoundException,
            OnlineImageNotFoundException, MalformedURLException,
            FontNotFoundException, LocalImageNotFoundException {
        if (!validateParameters()){
            return;
        }
        Tournament tournament = generateTournamentWithCurrentSettings();

        BufferedImage previewImage = thumbnailService
                .generatePreview(tournament, Game.SSBU, artType.getSelectionModel().getSelectedItem());
        Image image = SwingFXUtils.toFXImage(previewImage, null);
        preview.setImage(image);

    }


    protected Tournament generateTournamentWithCurrentSettings(){
        var lastSelectedArt = artType.getSelectionModel().getSelectedItem();
        for (var dir : artTypeDir){
            if(lastSelectedArt.equals(dir.getArtType())){
                var result = fighterImageSettingsFile.getText();
                if (result == null){
                    result = "";
                }
                dir.setFighterImageSettingsPath(result);
            }
        }
        var lastSelectedArtTop8 = artTypeTop8.getSelectionModel().getSelectedItem();
        for (var dir : artTypeDirTop8){
            if(lastSelectedArtTop8.equals(dir.getArtType())){
                var result = fighterImageSettingsFileTop8.getText();
                if (result == null){
                    result = "";
                }
                dir.setFighterImageSettingsPath(result);
            }
        }

        var textSettings = new TextSettings(id.getText(), font.getSelectionModel().getSelectedItem(),
                bold.isSelected(), italic.isSelected(), shadow.isSelected(), Float.parseFloat(contour.getText()),
                Integer.parseInt(sizeTop.getText()), Integer.parseInt(sizeBottom.getText()),
                Float.parseFloat(angleTop.getText()), Float.parseFloat(angleBottom.getText()),
                new int[]{Integer.parseInt(downOffsetTopLeft.getText()), Integer.parseInt(downOffsetTopRight.getText())},
                new int[]{Integer.parseInt(downOffsetBottomLeft.getText()), Integer.parseInt(downOffsetBottomRight.getText())});

        cacheGeneratedGraphicSettings(gameComboBox.getSelectionModel().getSelectedItem());
        var newFileThumbnailSettingsList = fileThumbnailSettingsList;
        var newFileTop8SettingsList = fileTop8SettingsList;
        for (FileThumbnailSettings settings : newFileThumbnailSettingsList){
            settings.setTextSettings(textSettings);
        }

        return new Tournament(id.getText(), name.getText(),
                logo.getText(), newFileThumbnailSettingsList, newFileTop8SettingsList);
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

        if (!missingParameters.toString().isEmpty()){
            missingParameters.append(System.lineSeparator());
        }
        return missingParameters.toString();
    }

    private void cacheGeneratedGraphicSettings(Game game){
        var thumbnailSettings = fileThumbnailSettingsList.stream()
                .filter(s -> game.equals(s.getGame()))
                .findFirst()
                .get();
        thumbnailSettings.setBackground(background.getText());
        thumbnailSettings.setForeground(foreground.getText());
        thumbnailSettings.getArtTypeDir().stream()
                .filter(a -> a.getArtType().equals(artType.getSelectionModel().getSelectedItem()))
                .findFirst()
                .get()
                .setFighterImageSettingsPath(fighterImageSettingsFile.getText());

        var top8Settings = fileTop8SettingsList.stream()
                .filter(s -> game.equals(s.getGame()))
                .findFirst()
                .get();
        top8Settings.setBackground(backgroundTop8.getText());
        top8Settings.setForeground(foregroundTop8.getText());
        top8Settings.setSlotSettingsFile(slotSettingsFileTop8.getText());
        top8Settings.getArtTypeDir().stream()
                .filter(a -> a.getArtType().equals(artTypeTop8.getSelectionModel().getSelectedItem()))
                .findFirst()
                .get()
                .setFighterImageSettingsPath(fighterImageSettingsFileTop8.getText());
    }

    private void loadCachedGeneratedGraphicSettings(Game game){
        var thumbnailSettings = fileThumbnailSettingsList.stream()
                .filter(s -> game.equals(s.getGame()))
                .findFirst()
                .get();
        background.setText(thumbnailSettings.getBackground());
        foreground.setText(thumbnailSettings.getBackground());
        artType.getSelectionModel().select(0);
        fighterImageSettingsFile.setText(thumbnailSettings.getArtTypeDir().stream()
                .filter(a -> a.getArtType().equals(artType.getSelectionModel().getSelectedItem()))
                .findFirst()
                .get()
                .getFighterImageSettingsPath());

        var top8Settings = fileTop8SettingsList.stream()
                .filter(s -> game.equals(s.getGame()))
                .findFirst()
                .get();
        backgroundTop8.setText(top8Settings.getBackground());
        foregroundTop8.setText(top8Settings.getBackground());
        slotSettingsFileTop8.setText(top8Settings.getSlotSettingsFile());
        artTypeTop8.getSelectionModel().select(0);
        fighterImageSettingsFileTop8.setText(top8Settings.getArtTypeDir().stream()
                .filter(a -> a.getArtType().equals(artTypeTop8.getSelectionModel().getSelectedItem()))
                .findFirst()
                .get()
                .getFighterImageSettingsPath());
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
