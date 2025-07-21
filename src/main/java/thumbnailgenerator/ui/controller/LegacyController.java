package thumbnailgenerator.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.LoadingType;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.service.GameEnumService;
import thumbnailgenerator.service.LegacyService;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.ui.loading.LoadingState;
import thumbnailgenerator.ui.textfield.ChosenJsonField;
import thumbnailgenerator.utils.converter.FighterArtTypeConverter;
import thumbnailgenerator.utils.converter.GameConverter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LegacyController implements Initializable {

    @Autowired
    private LegacyService legacyService;
    @Autowired
    private GameEnumService gameEnumService;
    @FXML
    private TextField versionTextField;
    @FXML
    private ComboBox<Game> gameComboBox;
    @FXML
    private ComboBox<FighterArtTypeEnum> artTypeComboBox;
    @FXML
    private ChosenJsonField fileChosen;
    @FXML
    private Label loadingText;
    @FXML
    private ProgressIndicator loadingIndicator;

    private static LoadingState loadingState;

    private static String OFFSET_VERSION_LIMIT = "4.3.0";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initDropdowns();
        initLoading();
    }

    private void initDropdowns(){
        val initialGame = Game.SSBU;
        gameComboBox.getItems().clear();
        artTypeComboBox.getItems().clear();

        gameComboBox.getItems().addAll(Game.values());
        gameComboBox.setConverter(new GameConverter());
        gameComboBox.getSelectionModel().select(Game.SSBU);
        gameComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    updateArtTypeComboBox(newValue);
                });
        updateArtTypeComboBox(initialGame);
        artTypeComboBox.setConverter(new FighterArtTypeConverter());
        artTypeComboBox.getSelectionModel().select(SmashUltimateFighterArtTypeEnum.RENDER);
    }

    private void updateArtTypeComboBox(Game game) {
        artTypeComboBox.getItems().clear();
        artTypeComboBox.getItems().addAll(
                gameEnumService.getAllFighterArtTypes(game)
        );
        artTypeComboBox.getSelectionModel().select(0);
    }

    private void initLoading(){
        loadingState = new LoadingState(false, LoadingType.THUMBNAIL_POSITION_CONVERSION, 0, 0);
        loadingText.visibleProperty().bind(loadingState.isLoadingProperty());
        loadingIndicator.visibleProperty().bind(loadingState.isLoadingProperty());
        loadingText.textProperty().bind(loadingState.getLoadingText());
    }

    public void convertFile(ActionEvent actionEvent)
            throws MalformedURLException, OnlineImageNotFoundException {
        convertCharacterOffsets();
    }

    private void convertCharacterOffsets()
            throws MalformedURLException, OnlineImageNotFoundException {
        if (isVersionBelowLimit(versionTextField.getText(), OFFSET_VERSION_LIMIT)) {
            var filePath = fileChosen.getText();
            var game = gameComboBox.getSelectionModel().getSelectedItem();
            var artTypeString =
                    artTypeComboBox.getSelectionModel().getSelectedItem();
            legacyService.convertThumbnailCharacterOffsets(filePath, game,
                    artTypeString, loadingState);
        } else {
            AlertFactory.displayWarning("Character offsets of versions above "
                    + OFFSET_VERSION_LIMIT + " do not need to be updated."
            );
        }
    }

    private int convertVersionNumber(String version){
        var mainVersion = version.replace(".","");
        return Integer.parseInt(mainVersion);
    }

    private boolean isVersionBelowLimit(String version, String versionLimit) {
        return convertVersionNumber(version) < convertVersionNumber(versionLimit);
    }
}
