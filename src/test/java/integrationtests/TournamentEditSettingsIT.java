package integrationtests;

import crosscutting.CustomApplicationTest;
import enums.ButtonId;
import enums.ChosenImageFieldId;
import enums.ChosenJsonFieldId;
import enums.ComboBoxId;
import enums.MenuId;
import enums.TextFieldId;
import enums.WindowId;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thumbnailgenerator.Main;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.FatalFuryCotwFighterArtTypeEnum;
import thumbnailgenerator.enums.GuiltyGearStriveFighterArtTypeEnum;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtTypeEnum;
import thumbnailgenerator.enums.SmashMeleeFighterArtTypeEnum;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.StreetFighter6FighterArtTypeEnum;
import thumbnailgenerator.enums.Tekken8FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.service.TournamentService;
import utils.FileUtils;
import utils.TestUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
public class TournamentEditSettingsIT extends CustomApplicationTest {

    @Autowired
    private TournamentService tournamentService;

    @BeforeEach
    public void setUp() throws IOException {
        FileUtils.createFileBackups();
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Replace the changed file with the original backup
        FileUtils.loadFileBackups();
    }

    @ParameterizedTest
    @MethodSource("getGamesAndDefaultArtTypeEnums")
    public void test_editTournamentWindow_opensWithCorrectValues_success(Game game, FighterArtTypeEnum artType)
            throws InterruptedException {
        //Arrange
        Tournament expectedTournament = TestUtils.getTournament(tournamentService, "invicta");

        //Act
        clickOnMenuOption(MenuId.EDIT);
        clickOnMenuOptionThenMove(MenuId.EDIT_TOURNAMENT, MenuId.EDIT_WEEKLY_L);
        clickOnMenuOption(MenuId.EDIT_INVICTA);

        WaitUtils.waitForWindowToLoad();
        Stage editStage = (Stage) getWindow(WindowId.EDIT_TOURNAMENT);
        Scene editScene = editStage.getScene();

        //Assert
        selectInComboBox(ComboBoxId.TOURNAMENT_GAME, game.getName());
        assertEquals(editScene.getRoot().getId(), WindowId.EDIT_TOURNAMENT.getValue());
        assertEquals(editStage.getTitle(), "Edit Tournament Smash Invicta");
        validateTournamentSettings(expectedTournament, editScene);
        validateTournamentThumbnailSettings(expectedTournament.getThumbnailSettingsByGame(game), editScene, artType);
        validateTournamentTop8Settings(expectedTournament.getTop8SettingsByGame(game), editScene, artType);
/* //SPLIT DUE TO BUG
        if(game.equals(Game.SSBU)) {
            scrollPaneVertically(ScrollPaneId.TOURNAMENT_SETTINGS, editScene, 1.0);

            selectInComboBox(ComboBoxId.TOURNAMENT_TOP8_ART_TYPE, SmashUltimateFighterArtType.MURAL.getValue());
            WaitUtils.waitInSeconds(1);
            assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_TOP8_ART_TYPE, editScene, SmashUltimateFighterArtType.MURAL.name());

            selectInComboBox(ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, SmashUltimateFighterArtType.MURAL.getValue());
            WaitUtils.waitInSeconds(1);
            assertEqualsComboBoxSelection(
                    ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, editScene, SmashUltimateFighterArtType.MURAL.name());

            validateTournamentThumbnailSettings(
                    expectedTournament.getThumbnailSettingsByGame(game), editScene, SmashUltimateFighterArtType.MURAL);
            validateTournamentTop8Settings(
                    expectedTournament.getTop8SettingsByGame(game), editScene, SmashUltimateFighterArtType.MURAL);
        }
        */

    }

    @Test
    public void test_editTournamentWindow_changeTournamentName_success()
            throws InterruptedException, IOException {
        //Arrange
        String newTournamentName = "NEW TEST NAME";
        File expectedTournamentSettings = FileUtils.getFileFromResources(
                "/expected/tournament/editedTournamentName.json"
        );
        File expectedTextSettings = FileUtils.getFileFromResources(
                "/expected/thumbnail/text/defaultText.json"
        );

        //Act
        clickOnMenuOption(MenuId.EDIT);
        clickOnMenuOptionThenMove(MenuId.EDIT_TOURNAMENT, MenuId.EDIT_WEEKLY_L);
        clickOnMenuOption(MenuId.EDIT_INVICTA);

        WaitUtils.waitForWindowToLoad();
        Stage editStage = (Stage) getWindow(WindowId.EDIT_TOURNAMENT);
        Scene editScene = editStage.getScene();
        writeInTextField(editScene, TextFieldId.TOURNAMENT_NAME, newTournamentName);
        clickOnButton(editScene, ButtonId.SAVE_TOURNAMENT);

        //Assert
        Tournament actualTournament = TestUtils.getTournament(tournamentService, "invicta");
        assertEquals(newTournamentName, actualTournament.getName());
        File actualTournamentSettings = FileUtils.loadTournamentsFile();
        FileUtils.assertSameTextFileContent(expectedTournamentSettings, actualTournamentSettings);
        File actualTextSettings = FileUtils.loadTextFile();
        FileUtils.assertSameTextFileContent(expectedTextSettings, actualTextSettings);
    }

    private void validateTournamentSettings(Tournament expectedTournament, Scene scene){
        assertEqualsTextFieldContent(TextFieldId.TOURNAMENT_NAME, scene, expectedTournament.getName());
        assertEqualsTextFieldContent(TextFieldId.TOURNAMENT_ID, scene, expectedTournament.getTournamentId());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_LOGO, scene, expectedTournament.getImage());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_THUMBNAIL_FONT, scene,
                expectedTournament.getThumbnailSettings().get(0).getTextSettings().getFont());
    }

    private void validateTournamentThumbnailSettings(
            FileThumbnailSettings expectedThumbnailSettings, Scene scene, FighterArtTypeEnum fighterArtTypeEnum){
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_FOREGROUND, scene,
                expectedThumbnailSettings.getForeground());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_BACKGROUND, scene,
                expectedThumbnailSettings.getBackground());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_THUMBNAIL_ART_TYPE, scene,
                fighterArtTypeEnum.toString());
        assertEqualsChosenJsonFieldContent(ChosenJsonFieldId.TOURNAMENT_THUMBNAIL_CHARACTER_SETTINGS, scene,
                expectedThumbnailSettings.getFighterImageSettingsFile(
                        fighterArtTypeEnum));
    }

    private void validateTournamentTop8Settings(
            FileTop8Settings expectedTop8Settings, Scene scene, FighterArtTypeEnum fighterArtTypeEnum){
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_TOP8_FOREGROUND, scene,
                expectedTop8Settings.getForeground());
        assertEqualsChosenImageFieldContent(ChosenImageFieldId.TOURNAMENT_TOP8_BACKGROUND, scene,
                expectedTop8Settings.getBackground());
        assertEqualsChosenJsonFieldContent(ChosenJsonFieldId.TOURNAMENT_TOP8_SLOT_SETTINGS, scene,
                expectedTop8Settings.getSlotSettingsFile());
        assertEqualsComboBoxSelection(ComboBoxId.TOURNAMENT_TOP8_ART_TYPE, scene,
                fighterArtTypeEnum.toString());
        assertEqualsChosenJsonFieldContent(ChosenJsonFieldId.TOURNAMENT_TOP8_CHARACTER_SETTINGS, scene,
                expectedTop8Settings.getFighterImageSettingsFile(
                        fighterArtTypeEnum));
    }

    private static Stream<Arguments> getGamesAndDefaultArtTypeEnums() {
        return Stream.of(
                Arguments.of(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER),
                Arguments.of(Game.SSBM, SmashMeleeFighterArtTypeEnum.HD),
                Arguments.of(Game.ROA2, RivalsOfAether2FighterArtTypeEnum.RENDER),
                Arguments.of(Game.SF6, StreetFighter6FighterArtTypeEnum.RENDER),
                Arguments.of(Game.TEKKEN8, Tekken8FighterArtTypeEnum.RENDER),
                Arguments.of(Game.GGST, GuiltyGearStriveFighterArtTypeEnum.RENDER),
                Arguments.of(Game.FFCOTW, FatalFuryCotwFighterArtTypeEnum.RENDER)
        );
    }
}
