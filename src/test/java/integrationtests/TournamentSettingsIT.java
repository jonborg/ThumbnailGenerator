package integrationtests;

import crosscutting.CustomApplicationTest;
import enums.ButtonId;
import enums.ChosenImageFieldId;
import enums.ChosenJsonFieldId;
import enums.ComboBoxId;
import enums.MenuId;
import enums.ScrollPaneId;
import enums.TextFieldId;
import enums.WindowId;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;
import utils.FileUtils;
import utils.TestUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TournamentSettingsIT extends CustomApplicationTest {

    @BeforeEach
    public void setUp() throws IOException {
        FileUtils.createFileBackups();
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Replace the changed file with the original backup
        FileUtils.loadFileBackups();
    }

    public void test_editTournamentWindow_opensWithCorrectValues_success() throws InterruptedException {
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
        validateTournamentThumbnailSettings(expectedTournament.getThumbnailSettingsByGame(Game.SSBU),
                editScene, SmashUltimateFighterArtType.RENDER);
        validateTournamentTop8Settings(expectedTournament.getTop8SettingsByGame(Game.SSBU),
                editScene, SmashUltimateFighterArtType.RENDER);

        selectInComboBox(ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, SmashUltimateFighterArtType.MURAL.getName());
        scrollPaneVertically(ScrollPaneId.TOURNAMENT_SETTINGS, editScene, 30);
        selectInComboBox(ComboBoxId.TOURNAMENT_TOP8_ART_TYPE, SmashUltimateFighterArtType.MURAL.getName());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, editScene,
                SmashUltimateFighterArtType.MURAL.toString());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_TOP8_ART_TYPE, editScene,
                SmashUltimateFighterArtType.MURAL.toString());
    }

    public void test_editTournamentWindow_changeTournamentName_success()
            throws InterruptedException, IOException {
        //Arrange
        Tournament tournament = TestUtils.getTournament("invicta");
        String newTournamentName = "NEW TEST NAME";
        File editedTournamentSettings = FileUtils.getFileFromResources(
                "/expected/tournament/editedTournamentName.json"
        );

        //Act
        clickOnMenuOption(MenuId.EDIT);
        clickOnMenuOptionThenMove(MenuId.EDIT_TOURNAMENT, MenuId.EDIT_WEEKLY_L);
        clickOnMenuOption(MenuId.EDIT_INVICTA);

        WaitUtils.waitForWindowToLoad();
        Stage editStage = (Stage) getWindow(WindowId.EDIT_TOURNAMENT);
        Scene editScene = editStage.getScene();
        writeInTextField(editScene, TextFieldId.TOURNAMENT_NAME, newTournamentName);
        clickOnButton(editScene, ButtonId.SAVE_THUMBNAIL);

        //Assert
        Tournament actualTournament = TestUtils.getTournament("invicta");
        assertEquals(newTournamentName, actualTournament.getName());
        File actualTournamentSettings = FileUtils.loadTournamentsFile();
        FileUtils.assertSameFileContent(editedTournamentSettings, actualTournamentSettings);
    }

    private void validateTournamentSettings(Tournament expectedTournament, Scene scene){
        assertEqualsTextFieldContent(TextFieldId.TOURNAMENT_NAME, scene, expectedTournament.getName());
        assertEqualsTextFieldContent(TextFieldId.TOURNAMENT_ID, scene, expectedTournament.getTournamentId());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_LOGO, scene, expectedTournament.getImage());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_THUMBNAIL_FONT, scene,
                expectedTournament.getThumbnailSettings().get(0).getTextSettings().getFont());
    }

    private void validateTournamentThumbnailSettings(
            FileThumbnailSettings expectedThumbnailSettings, Scene scene, FighterArtType fighterArtType){
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_FOREGROUND, scene,
                expectedThumbnailSettings.getForeground());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_BACKGROUND, scene,
                expectedThumbnailSettings.getBackground());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, scene,
                fighterArtType.toString());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_CHARACTER_SETTINGS, scene,
                expectedThumbnailSettings.getFighterImageSettingsFile(fighterArtType));
    }

    private void validateTournamentTop8Settings(
            FileTop8Settings expectedTop8Settings, Scene scene, FighterArtType fighterArtType){
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
