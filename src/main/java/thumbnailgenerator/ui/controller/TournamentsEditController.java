package thumbnailgenerator.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.service.TournamentService;
import thumbnailgenerator.ui.factory.alert.AlertFactory;

@Controller
public class TournamentsEditController extends TournamentsCreateController {
    private static final Logger LOGGER = LogManager.getLogger(TournamentsEditController.class);
    @Autowired
    private TournamentService tournamentService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var tournament = getSelectedEdit();
        var thumbnailSettings = tournamentService.getTournamentThumbnailSettingsOrDefault(tournament, Game.SSBU);
        var top8Settings = tournamentService.getTournamentTop8SettingsOrDefault(tournament, Game.SSBU);
        var thumbnailRenderSettings = thumbnailSettings
                .getFighterImageSettingsFile(SmashUltimateFighterArtTypeEnum.RENDER);
        var top8RenderSettings = top8Settings
                .getFighterImageSettingsFile(SmashUltimateFighterArtTypeEnum.RENDER);
        var textSettings = thumbnailSettings.getTextSettings();
        LOGGER.info("Editing tournament -> {}", tournament.toString());

        initGamesDropdown(tournament);
        initNumberTextFields();
        initFontDropdown();
        initFighterArtTypeDropdown();

        id.setText(tournament.getTournamentId());
        name.setText(tournament.getName());
        logo.setText(tournament.getImage());

        var thumbnailForeground = thumbnailSettings.getThumbnailForeground();
        foreground.setText(thumbnailForeground.getForeground());
        customForeground.setSelected(thumbnailForeground.isCustomForeground());

        thumbnailPrimaryColor.setValue(Color.web(thumbnailForeground.getColors().get("primary")));
        thumbnailSecondaryColor.setValue(Color.web(thumbnailForeground.getColors().get("secondary")));

        foregroundVersus.setText(thumbnailForeground.getThumbnailForegroundVersus().getImagePath());
        foregroundVersusScale.setText(String.valueOf(thumbnailForeground.getThumbnailForegroundVersus().getScale()));
        foregroundVersusOffset.setText(String.valueOf(thumbnailForeground.getThumbnailForegroundVersus().getVerticalOffset()));

        foregroundLogo.setText(thumbnailForeground.getThumbnailForegroundLogo().getImagePath());
        foregroundLogoScale.setText(String.valueOf(thumbnailForeground.getThumbnailForegroundLogo().getScale()));
        foregroundLogoOffset.setText(String.valueOf(thumbnailForeground.getThumbnailForegroundLogo().getVerticalOffset()));
        foregroundLogoAbove.setSelected(thumbnailForeground.getThumbnailForegroundLogo().isAboveForeground());

        background.setText(thumbnailSettings.getBackground());
        fighterImageSettingsFile.setText(thumbnailRenderSettings);

        foregroundTop8.setText(top8Settings.getForeground());
        backgroundTop8.setText(top8Settings.getBackground());
        slotSettingsFileTop8.setText(top8Settings.getSlotSettingsFile());
        fighterImageSettingsFileTop8.setText(top8RenderSettings);

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
            //throw e;
        }
    }

    @Override
    public void save(ActionEvent actionEvent) {
        LOGGER.info("Saving changes to tournament.");
        Tournament currentTournament = generateTournamentWithCurrentSettings();
        LOGGER.debug("Saving changes to tournament -> {}", currentTournament.toString());
        tournamentService
                .saveChangesToTournament(currentTournament, getSelectedEdit());
        onSaveCallback.run();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void cancel(ActionEvent actionEvent)  {
        LOGGER.info("User cancelled tournament editing. Changes will not be saved.");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private Tournament getSelectedEdit(){
        return tournamentService.getSelectedEdit();
    }
}
