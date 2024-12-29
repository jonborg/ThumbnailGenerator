package integrationtests;

import crosscutting.CustomApplicationTest;
import enums.ChosenImageFieldId;
import enums.ChosenJsonFieldId;
import enums.ComboBoxId;
import enums.MenuId;
import enums.ScrollPaneId;
import enums.TextFieldId;
import enums.WindowId;
import fighter.FighterArtType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import thumbnail.text.TextSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import tournament.settings.ThumbnailSettings;
import tournament.settings.Top8Settings;
import ui.controller.ThumbnailGeneratorController;
import utils.TestUtils;
import utils.WaitUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TournamentSettingsIT extends CustomApplicationTest {

    @Override
    public void start(Stage stage) throws IOException {
        TournamentUtils.initTournamentsListAndSettings();
        FXMLLoader loader = new FXMLLoader(
                ThumbnailGeneratorController.class.getClassLoader().getResource(
                        "ui/fxml/thumbnailGenerator.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @Test
    public void test_editTournamentWindowOpensWithCorrectValues_success() throws InterruptedException {
        //Arrange
        Tournament expectedTournament = TestUtils.getTournament("invicta");

        //Act
        clickOnMenuOption(MenuId.EDIT);
        clickOnMenuOptionThenMove(MenuId.EDIT_TOURNAMENT, MenuId.EDIT_WEEKLY_L);
        clickOnMenuOption(MenuId.EDIT_INVICTA);

        WaitUtils.waitForWindowToLoad();
        Stage editStage = (Stage) getWindow(WindowId.EDIT_TOURNAMENT);
        Scene editScene = editStage.getScene();

        //Assert
        assertEquals(editScene.getRoot().getId(), WindowId.EDIT_TOURNAMENT.getValue());
        assertEquals(editStage.getTitle(), "Edit Tournament Smash Invicta");
        validateTournamentSettings(expectedTournament, editScene);
        validateTournamentThumbnailSettings(expectedTournament.getThumbnailSettings(), editScene, FighterArtType.RENDER);
        validateTournamentTop8Settings(expectedTournament.getTop8Settings(), editScene, FighterArtType.RENDER);

        selectInComboBox(ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, FighterArtType.MURAL.getName());
        scrollPaneVertically(ScrollPaneId.TOURNAMENT_SETTINGS, editScene, 30);
        selectInComboBox(ComboBoxId.TOURNAMENT_TOP8_ART_TYPE, FighterArtType.MURAL.getName());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, editScene,
                FighterArtType.MURAL.toString());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_TOP8_ART_TYPE, editScene,
                FighterArtType.MURAL.toString());
    }

    private void validateTournamentSettings(Tournament expectedTournament, Scene scene){
        assertEqualsTextFieldContent(TextFieldId.TOURNAMENT_NAME, scene, expectedTournament.getName());
        assertEqualsTextFieldContent(TextFieldId.TOURNAMENT_ID, scene, expectedTournament.getTournamentId());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_LOGO, scene, expectedTournament.getImage());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_THUMBNAIL_FONT, scene,
                expectedTournament.getThumbnailSettings().getTextSettings().getFont());
    }

    private void validateTournamentThumbnailSettings(ThumbnailSettings expectedThumbnailSettings, Scene scene, FighterArtType fighterArtType){
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_FOREGROUND, scene,
                expectedThumbnailSettings.getForeground());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_BACKGROUND, scene,
                expectedThumbnailSettings.getBackground());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, scene,
                fighterArtType.toString());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_CHARACTER_SETTINGS, scene,
                expectedThumbnailSettings.getFighterImageSettingsFile(fighterArtType));
    }

    private void validateTournamentTop8Settings(Top8Settings expectedTop8Settings, Scene scene, FighterArtType fighterArtType){
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_TOP8_FOREGROUND, scene,
                expectedTop8Settings.getForeground());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_TOP8_BACKGROUND, scene,
                expectedTop8Settings.getBackground());
        assertEqualsChosenJsonFieldContent(ChosenJsonFieldId.TOURNAMENT_TOP8_SLOT_SETTINGS, scene,
                expectedTop8Settings.getSlotSettingsFile());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_TOP8_ART_TYPE, scene,
                fighterArtType.toString());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_TOP8_CHARACTER_SETTINGS, scene,
                expectedTop8Settings.getFighterImageSettingsFile(fighterArtType));
    }

}
