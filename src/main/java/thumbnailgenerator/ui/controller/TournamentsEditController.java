package thumbnailgenerator.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
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
        var thumbnailRenderSettings = tournament.getThumbnailSettingsByGame(Game.SSBU)
                .getFighterImageSettingsFile(SmashUltimateFighterArtType.RENDER);
        var top8RenderSettings = tournament.getTop8SettingsByGame(Game.SSBU)
                .getFighterImageSettingsFile(SmashUltimateFighterArtType.RENDER);
        LOGGER.info("Editing tournament -> {}", tournament.toString());

        initGamesDropdown(tournament);
        initNumberTextFields();
        initFontDropdown();
        initFighterArtTypeDropdown(thumbnailRenderSettings);

        id.setText(tournament.getTournamentId());
        name.setText(tournament.getName());
        logo.setText(tournament.getImage());
        foreground.setText(tournament.getThumbnailSettingsByGame(Game.SSBU).getForeground());
        background.setText(tournament.getThumbnailSettingsByGame(Game.SSBU).getBackground());
        fighterImageSettingsFile.setText(thumbnailRenderSettings);
        if(tournament.getThumbnailSettingsByGame(Game.SSBU).getArtTypeDir() != null
                && !tournament.getThumbnailSettingsByGame(Game.SSBU).getArtTypeDir().isEmpty()) {
            artTypeDirThumbnail = new ArrayList<>();
            tournament.getThumbnailSettingsByGame(Game.SSBU)
                    .getArtTypeDir().forEach(dir -> artTypeDirThumbnail.add(dir.clone()));
        }
        foregroundTop8.setText(tournament.getTop8SettingsByGame(Game.SSBU).getForeground());
        backgroundTop8.setText(tournament.getTop8SettingsByGame(Game.SSBU).getBackground());
        slotSettingsFileTop8.setText(tournament.getTop8SettingsByGame(Game.SSBU).getSlotSettingsFile());
        fighterImageSettingsFileTop8.setText(top8RenderSettings);
        if(tournament.getTop8SettingsByGame(Game.SSBU).getArtTypeDir() != null
                && !tournament.getTop8SettingsByGame(Game.SSBU).getArtTypeDir().isEmpty()) {
            artTypeDirTop8 = new ArrayList<>();
            tournament.getTop8SettingsByGame(Game.SSBU)
                    .getArtTypeDir().forEach(dir -> artTypeDirTop8.add(dir.clone()));
        }

        TextSettings textSettings = tournament.getThumbnailSettingsByGame(Game.SSBU).getTextSettings();
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
