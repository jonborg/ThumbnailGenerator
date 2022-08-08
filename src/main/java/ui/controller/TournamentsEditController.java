package ui.controller;

import fighter.FighterArtType;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import thumbnail.text.TextSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

public class TournamentsEditController extends TournamentsCreateController {
    private static final Logger LOGGER = LogManager.getLogger(TournamentsEditController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tournament tournament = getSelectedEdit();
        var renderSettingsFile = tournament.getFighterImageSettingsFile(FighterArtType.RENDER);
        LOGGER.info("Editing tournament -> {}", tournament.toString());

        initNumberTextFields();
        initFontDropdown();
        initFighterArtTypeDropdown(renderSettingsFile);

        id.setText(tournament.getTournamentId());
        name.setText(tournament.getName());
        logo.setText(tournament.getImage());
        foreground.setText(tournament.getForeground());
        background.setText(tournament.getBackground());
        fighterImageSettingsFile.setText(renderSettingsFile);
        if(tournament.getArtTypeDir() != null
                && !tournament.getArtTypeDir().isEmpty()) {
            artTypeDir = new ArrayList<>();
            tournament.getArtTypeDir().forEach(dir -> artTypeDir.add(dir.clone()));
        }

        TextSettings textSettings = tournament.getTextSettings();
        try {
            font.getSelectionModel().select(textSettings.getFont());
            sizeTop.setText(String.valueOf(textSettings.getSizeTop()));
            sizeBottom.setText(String.valueOf(textSettings.getSizeBottom()));
            angleTop.setText(String.valueOf(textSettings.getAngleTop()));
            angleBottom.setText(String.valueOf(textSettings.getAngleBottom()));
            bold.setSelected(textSettings.isBold());
            italic.setSelected(textSettings.isItalic());
            shadow.setSelected(textSettings.isShadow());
            contour.setText(String.valueOf(textSettings.getContour()));
            downOffsetTopLeft.setText(String.valueOf(textSettings.getDownOffsetTop()[0]));
            downOffsetTopRight.setText(String.valueOf(textSettings.getDownOffsetTop()[1]));
            downOffsetBottomLeft.setText(String.valueOf(textSettings.getDownOffsetBottom()[0]));
            downOffsetBottomRight.setText(String.valueOf(textSettings.getDownOffsetBottom()[1]));
        }catch(Exception e){
            LOGGER.error("Could not load text settings for tournament {}.", tournament.getName());
            LOGGER.catching(e);
            AlertFactory.displayError("Could not load text settings for tournament " + tournament.getName()
                            + ". Please check text settings file for setting for tournament with id of "
                            + tournament.getTournamentId(), ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public void save(ActionEvent actionEvent) {
        LOGGER.info("Saving changes to tournament.");
        Tournament currentTournament = generateTournamentWithCurrentSettings();
        LOGGER.debug("Saving changes to tournament -> {}", currentTournament.toString());
        if(!getSelectedEdit().updateDifferences(currentTournament)){
            updateTournamentsListAndSettings();
        }
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void cancel(ActionEvent actionEvent)  {
        LOGGER.info("User cancelled tournament editing. Changes will not be saved.");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private static Tournament getSelectedEdit(){
        return TournamentUtils.getSelectedEdit();
    }
}
