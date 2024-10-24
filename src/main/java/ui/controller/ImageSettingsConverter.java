package ui.controller;

import converter.ThumbnailFighterImageSettingsConverter;
import converter.Top8FighterImageSettingsConverter;
import fighter.FighterArtType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import ui.factory.alert.AlertFactory;
import ui.textfield.ChosenJsonField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ImageSettingsConverter implements Initializable {
    private final Logger LOGGER = LogManager.getLogger(ThumbnailGeneratorController.class);
    @FXML
    private ChosenJsonField imageSettingsFile;
    @FXML
    private ComboBox<String> imageType;
    @FXML
    private ComboBox<FighterArtType> artType;
    @FXML
    private ComboBox<String> version;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initImageTypeDropdown();
        ControllerUtils.initArtDropdown(artType);
        initVersionDropdown();
    }
    private void initImageTypeDropdown() {
        imageType.getItems().addAll(Arrays.asList("THUMBNAIL", "TOP8"));
        imageType.getSelectionModel().select("THUMBNAIL");
    }
    private void initVersionDropdown() {
        version.getItems().addAll(Arrays.asList("v3.0.0"));
        version.getSelectionModel().select("v3.0.0");
        version.setDisable(true);
    }

    public void convertFighterImageSettings(ActionEvent actionEvent) {
        LOGGER.info("User chose to convert fighter image settings.");
        File selectedFile = new File(imageSettingsFile.getText());
        FighterArtType selectedArtType = artType.getSelectionModel().getSelectedItem();
        String selectedImageType = imageType.getSelectionModel().getSelectedItem();
        LOGGER.info("User wants to convert file {} for {} with art type {}.", selectedFile.getPath(), selectedImageType, selectedArtType.getName());
        try {
            switch (selectedImageType) {
                case "THUMBNAIL":
                    ThumbnailFighterImageSettingsConverter
                            .convertThumbnailImageSettings(selectedFile,
                                    selectedArtType);
                    AlertFactory.displayInfo(
                            "Successfully converted file. File saved with \"Converted\" suffix in the same directory. Please change tournament settings to use new file.");
                    break;
                case "TOP8":
                    Top8FighterImageSettingsConverter
                            .convertTop8ImageSettings(selectedFile,
                                    selectedArtType);
                    AlertFactory.displayInfo(
                            "Successfully converted file. File saved with \"Converted\" suffix in the same directory. Please change tournament settings to use new file.");
                default:
                    throw new IOException();
            }
        }catch(IOException e){
            AlertFactory.displayError("Could not convert file " + selectedFile.getName() + ". ",
                    ExceptionUtils.getStackTrace(e));
        }
    }
}
