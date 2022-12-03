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
        var tournament = getSelectedEdit();
        var thumbnailRenderSettings = tournament.getThumbnailSettings()
                .getFighterImageSettingsFile(FighterArtType.RENDER);
        var top8RenderSettings = tournament.getTop8Settings()
                .getFighterImageSettingsFile(FighterArtType.RENDER);
        LOGGER.info("Editing tournament -> {}", tournament.toString());

        initNumberTextFields();
        initFontDropdown();
        initFighterArtTypeDropdown(thumbnailRenderSettings);

        id.setText(tournament.getTournamentId());
        name.setText(tournament.getName());
        logo.setText(tournament.getImage());
        foreground.setText(tournament.getThumbnailSettings().getForeground());
        background.setText(tournament.getThumbnailSettings().getBackground());
        fighterImageSettingsFile.setText(thumbnailRenderSettings);
        if(tournament.getThumbnailSettings().getArtTypeDir() != null
                && !tournament.getThumbnailSettings().getArtTypeDir().isEmpty()) {
            artTypeDir = new ArrayList<>();
            tournament.getThumbnailSettings()
                    .getArtTypeDir().forEach(dir -> artTypeDir.add(dir.clone()));
        }
        foregroundTop8.setText(tournament.getTop8Settings().getForeground());
        backgroundTop8.setText(tournament.getTop8Settings().getBackground());
        slotSettingsFileTop8.setText(tournament.getTop8Settings().getSlotSettingsFile());
        fighterImageSettingsFileTop8.setText(top8RenderSettings);
        if(tournament.getTop8Settings().getArtTypeDir() != null
                && !tournament.getTop8Settings().getArtTypeDir().isEmpty()) {
            artTypeDirTop8 = new ArrayList<>();
            tournament.getTop8Settings()
                    .getArtTypeDir().forEach(dir -> artTypeDirTop8.add(dir.clone()));
        }

        TextSettings textSettings = tournament.getThumbnailSettings().getTextSettings();
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
