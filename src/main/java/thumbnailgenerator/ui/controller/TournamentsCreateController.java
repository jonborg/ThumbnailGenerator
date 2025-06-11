package thumbnailgenerator.ui.controller;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import thumbnailgenerator.dto.FighterArtSettings;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.ThumbnailForeground;
import thumbnailgenerator.dto.ThumbnailForegroundLogo;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.exception.FontNotFoundException;
import thumbnailgenerator.exception.LocalImageNotFoundException;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.service.GameEnumService;
import thumbnailgenerator.service.ThumbnailService;
import thumbnailgenerator.utils.converter.GameConverter;
import thumbnailgenerator.utils.converter.FighterArtTypeConverter;
import thumbnailgenerator.service.TournamentService;
import thumbnailgenerator.ui.combobox.InputFilter;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.ui.textfield.ChosenImageField;
import thumbnailgenerator.ui.textfield.ChosenJsonField;
import thumbnailgenerator.utils.javafx.ColorUtils;

@Primary
@Controller
public class TournamentsCreateController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(TournamentsCreateController.class);
    @FXML
    protected ComboBox<Game> tournamentGame;
    @FXML
    protected TextField name;
    @FXML
    protected TextField id;
    @FXML
    protected ChosenImageField logo;
    @FXML
    protected CheckBox customForeground;
    @FXML
    protected ChosenImageField foreground;
    @FXML
    protected ColorPicker thumbnailPrimaryColor;
    @FXML
    protected ColorPicker thumbnailSecondaryColor;
    @FXML
    protected ChosenImageField foregroundLogo;
    @FXML
    protected TextField foregroundLogoScale;
    @FXML
    protected TextField foregroundLogoOffset;
    @FXML
    protected CheckBox foregroundLogoAbove;
    @FXML
    protected ChosenImageField background;
    @FXML
    protected ComboBox<FighterArtTypeEnum> artTypeThumbnail;
    @FXML
    protected ChosenJsonField fighterImageSettingsFile;
    @FXML
    protected ChosenImageField foregroundTop8;
    @FXML
    protected ChosenImageField backgroundTop8;
    @FXML
    protected ComboBox<FighterArtTypeEnum> artTypeTop8;
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
    protected List<FileThumbnailSettings> fileThumbnailSettingsList;
    protected List<FileTop8Settings> fileTop8SettingsList;
    private @Autowired TournamentService tournamentService;
    private @Autowired ThumbnailService thumbnailService;
    private @Autowired GameEnumService gameEnumService;
    protected Runnable onSaveCallback;

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Creating new tournament.");
        initGamesDropdown(null);
        initNumberTextFields();
        initFontDropdown();
        initFighterArtTypeDropdown();
    }

    private List<FighterArtSettings> initArtType(Game game, boolean hasDefaultSettingsFile){
        List<FighterArtSettings> list = new ArrayList<>();
        for (FighterArtTypeEnum artType : gameEnumService.getAllFighterArtTypes(game)) {
            String defaultSettingsFile = hasDefaultSettingsFile ?
                    gameEnumService.getDefaultFighterArtTypeSettingsFile(game, artType) : "";
            list.add(FighterArtSettings.builder()
                    .artType(artType)
                    .fighterImageSettingsPath(defaultSettingsFile)
                    .build());
        }
        return list;
    }

    protected void initGamesDropdown(Tournament tournament){
        tournamentGame.getItems().addAll(Game.values());
        tournamentGame.setConverter(new GameConverter());
        tournamentGame.getSelectionModel().select(Game.SSBU);
        fileThumbnailSettingsList = new ArrayList<>();
        fileTop8SettingsList = new ArrayList<>();
        for (Game game: Game.values()) {
            fileThumbnailSettingsList
                    .add(new FileThumbnailSettings(game,
                            new ThumbnailForeground("", new HashMap<>(),
                                    new ThumbnailForegroundLogo("", 1.0f, 0, false),
                                    false), "",
                            initArtType(game, true), null));
            fileTop8SettingsList
                    .add(new FileTop8Settings(game,
                            "", "",
                            initArtType(game, false), null));
        }
        if (tournament != null) {
            for (FileThumbnailSettings settings : tournament.getThumbnailSettings()){
                var settingToUpdate = fileThumbnailSettingsList.stream()
                        .filter(s -> s.getGame().equals(settings.getGame()))
                        .findFirst()
                        .orElse(null);
                fileThumbnailSettingsList.remove(settingToUpdate);
                fileThumbnailSettingsList.add(settings.clone());
            }
            for (FileTop8Settings settings : tournament.getTop8Settings()){
                var settingToUpdate = fileTop8SettingsList.stream()
                        .filter(s -> s.getGame().equals(settings.getGame()))
                        .findFirst()
                        .orElse(null);
                fileTop8SettingsList.remove(settingToUpdate);
                fileTop8SettingsList.add(settings.clone());
            }
        }

        tournamentGame.getSelectionModel().selectedItemProperty()
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
                foregroundLogoOffset,
                sizeTop, sizeBottom,
                downOffsetTopLeft, downOffsetTopRight, downOffsetBottomLeft, downOffsetBottomRight)
        );
        List<TextField> listFloatFields = new ArrayList<>(Arrays.asList(
                foregroundLogoScale, angleTop, angleBottom, contour)
        );

        for (TextField intField : listIntFields){
            intField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
            intField.focusedProperty().addListener((arg0,newValue, oldValue) -> emptyTextField(intField));
        }
        for (TextField floatField : listFloatFields){
            floatField.setTextFormatter(new TextFormatter<>(new FloatStringConverter(), 0.0f, floatFilter));
            floatField.focusedProperty().addListener((arg0,newValue, oldValue) -> emptyTextField(floatField));
        }
        foregroundLogoScale.setTextFormatter(new TextFormatter<>(new FloatStringConverter(), 1.0f, floatFilter));
        foregroundLogoScale.focusedProperty().addListener((arg0,newValue, oldValue) -> emptyTextField(foregroundLogoScale));

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

    protected void initFighterArtTypeDropdown(){
        var game = tournamentGame.getSelectionModel().getSelectedItem();
        var currentThumbnailSettings = fileThumbnailSettingsList.stream().filter(t -> t.getGame().equals(game)).findFirst().get();
        var currentFighterArtTypeValues = gameEnumService.getAllFighterArtTypes(game);
        var currentDefaultArtType = gameEnumService.getDefaultArtType(game);
        fighterImageSettingsFile.setText(gameEnumService.getDefaultFighterArtTypeSettingsFile(game, currentDefaultArtType));
        artTypeThumbnail.getItems().addAll(currentFighterArtTypeValues);
        artTypeThumbnail.setConverter(new FighterArtTypeConverter());
        artTypeThumbnail.getSelectionModel().select(currentDefaultArtType);
        artTypeThumbnail.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
                    for (var dir : currentThumbnailSettings.getArtTypeDir()){
                        if (Objects.equals(oldValue,dir.getArtType())){
                            dir.setFighterImageSettingsPath(
                                    fighterImageSettingsFile.getText());
                        }
                    }
                    for (var dir : currentThumbnailSettings.getArtTypeDir()){
                        if (Objects.equals(newValue, dir.getArtType())){
                            fighterImageSettingsFile.setText(dir.getFighterImageSettingsPath());
                        }
                    }
                });

        var currentTop8Settings = fileTop8SettingsList.stream().filter(t -> t.getGame().equals(game)).findFirst().get();
        artTypeTop8.getItems().addAll(currentFighterArtTypeValues);
        artTypeTop8.setConverter(new FighterArtTypeConverter());
        artTypeTop8.getSelectionModel().select(currentDefaultArtType);
        artTypeTop8.getSelectionModel().selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
                    for (var dir : currentTop8Settings.getArtTypeDir()){
                        if (Objects.equals(oldValue, dir.getArtType())){
                            dir.setFighterImageSettingsPath(
                                    fighterImageSettingsFileTop8.getText());
                        }
                    }
                    for (var dir : currentTop8Settings.getArtTypeDir()){
                        if (Objects.equals(newValue, dir.getArtType())){
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
        Game game = tournamentGame.getSelectionModel().getSelectedItem();
        FighterArtTypeEnum artType = artTypeThumbnail.getSelectionModel().getSelectedItem();
        BufferedImage previewImage = thumbnailService
                .generatePreview(tournament, game, artType);
        Image image = SwingFXUtils.toFXImage(previewImage, null);
        preview.setImage(image);
    }


    protected Tournament generateTournamentWithCurrentSettings(){
        var lastSelectedGame = tournamentGame.getSelectionModel().getSelectedItem();
        var lastSelectedArt = artTypeThumbnail.getSelectionModel().getSelectedItem();
        var currentThumbnailSettings = fileThumbnailSettingsList.stream().filter(t -> t.getGame().equals(lastSelectedGame)).findFirst().get();
        for (var dir : currentThumbnailSettings.getArtTypeDir()){
            if(lastSelectedArt.equals(dir.getArtType())){
                var result = fighterImageSettingsFile.getText();
                if (result == null){
                    result = "";
                }
                dir.setFighterImageSettingsPath(result);
            }
        }

        var currentTop8Settings = fileTop8SettingsList.stream().filter(t -> t.getGame().equals(lastSelectedGame)).findFirst().get();
        var lastSelectedArtTop8 = artTypeTop8.getSelectionModel().getSelectedItem();
        for (var dir : currentTop8Settings.getArtTypeDir()){
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

        cacheGeneratedGraphicSettings(tournamentGame.getSelectionModel().getSelectedItem());
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
        var thumbnailForeground = thumbnailSettings.getThumbnailForeground();
        thumbnailForeground.setForeground(foreground.getText());
        thumbnailForeground.setCustomForeground(customForeground.isSelected());
        thumbnailForeground.getColors().put("primary", ColorUtils.getHex(thumbnailPrimaryColor.getValue()));
        thumbnailForeground.getColors().put("secondary", ColorUtils.getHex(thumbnailSecondaryColor.getValue()));
        thumbnailForeground.getThumbnailForegroundLogo().setLogo(foregroundLogo.getText());
        thumbnailForeground.getThumbnailForegroundLogo().setScale(Float.parseFloat(foregroundLogoScale.getText()));
        thumbnailForeground.getThumbnailForegroundLogo().setVerticalOffset(Integer.parseInt(foregroundLogoOffset.getText()));
        thumbnailForeground.getThumbnailForegroundLogo().setAboveForeground(foregroundLogoAbove.isSelected());
        thumbnailSettings.getArtTypeDir().stream()
                .filter(a -> a.getArtType().equals(artTypeThumbnail.getSelectionModel().getSelectedItem()))
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
        var thumbnailForeground = thumbnailSettings.getThumbnailForeground();
        background.setText(thumbnailSettings.getBackground());
        foreground.setText(thumbnailForeground.getForeground());
        customForeground.setSelected(thumbnailForeground.isCustomForeground());
        thumbnailPrimaryColor.setValue(Color.web(thumbnailForeground.getColors().get("primary")));
        thumbnailSecondaryColor.setValue(Color.web(thumbnailForeground.getColors().get("secondary")));
        foregroundLogo.setText(thumbnailForeground.getThumbnailForegroundLogo().getLogo());
        foregroundLogoScale.setText(String.valueOf(thumbnailForeground.getThumbnailForegroundLogo().getScale()));
        foregroundLogoOffset.setText(String.valueOf(thumbnailForeground.getThumbnailForegroundLogo().getVerticalOffset()));
        foregroundLogoAbove.setSelected(thumbnailForeground.getThumbnailForegroundLogo().isAboveForeground());
        artTypeThumbnail.getItems().clear();
        artTypeThumbnail.getItems().addAll(gameEnumService.getAllFighterArtTypes(game));
        artTypeThumbnail.getSelectionModel().select(0);
        fighterImageSettingsFile.setText(thumbnailSettings.getArtTypeDir().stream()
                .filter(a -> a.getArtType().equals(artTypeThumbnail.getSelectionModel().getSelectedItem()))
                .findFirst()
                .get()
                .getFighterImageSettingsPath());

        var top8Settings = fileTop8SettingsList.stream()
                .filter(s -> game.equals(s.getGame()))
                .findFirst()
                .get();
        backgroundTop8.setText(top8Settings.getBackground());
        foregroundTop8.setText(top8Settings.getForeground());
        slotSettingsFileTop8.setText(top8Settings.getSlotSettingsFile());
        artTypeTop8.getItems().clear();
        artTypeTop8.getItems().addAll(gameEnumService.getAllFighterArtTypes(game));
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
        tournamentService.saveNewTournaments(currentTournament);
        onSaveCallback.run();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void cancel(ActionEvent actionEvent)  {
        LOGGER.info("User cancelled tournament creation. Tournament will not be saved.");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
