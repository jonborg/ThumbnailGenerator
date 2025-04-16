package integrationtests;

import crosscutting.CustomApplicationTest;
import enums.ButtonId;
import enums.CheckBoxId;
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
import thumbnailgenerator.enums.RivalsOfAether2FighterArtType;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.StreetFighter6FighterArtType;
import thumbnailgenerator.enums.Tekken8FighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;
import thumbnailgenerator.service.TournamentService;
import utils.FileUtils;
import utils.TestUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
public class TournamentCreateSettingsIT extends CustomApplicationTest {

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
    public void test_createTournament_success(Game game, FighterArtType artType)
            throws InterruptedException, IOException {
        //Arrange
        Tournament expectedTournament = new Tournament(
                TestUtils.getTournament(tournamentService, "weeklyl"),
                "createITTest"
        );
        var expectedThumbnailSettings = expectedTournament.getThumbnailSettingsByGame(game);
        var expectedTop8Settings = expectedTournament.getTop8SettingsByGame(game);
        var expectedTextSettings = expectedThumbnailSettings.getTextSettings();
        var expectedTournamentFile = FileUtils
                .getFileFromResources("/expected/tournament/newTournament"+game.name()+".json");
        var expectedTextSettingsFile = FileUtils
                .getFileFromResources("/expected/thumbnail/text/newTournamentText.json");

        //Act
        clickOnMenuOption(MenuId.EDIT);
        clickOnMenuOption(MenuId.CREATE_TOURNAMENT);

        WaitUtils.waitForWindowToLoad();
        Stage createStage = (Stage) getWindow(WindowId.CREATE_TOURNAMENT);
        Scene createScene = createStage.getScene();

        writeInTextField(TextFieldId.TOURNAMENT_NAME, expectedTournament.getName());
        writeInTextField(TextFieldId.TOURNAMENT_ID, expectedTournament.getTournamentId());
        writeInComboBox(ComboBoxId.TOURNAMENT_THUMBNAIL_FONT, expectedTextSettings.getFont());
        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_LOGO, expectedTournament.getImage());

        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_TOP_SIZE,
                expectedTextSettings.getSizeTop());
        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_TOP_ANGLE,
                expectedTextSettings.getAngleTop());
        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_BOTTOM_SIZE,
                expectedTextSettings.getSizeBottom());
        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_BOTTOM_ANGLE,
                expectedTextSettings.getAngleBottom());

        setCheckBox(CheckBoxId.TOURNAMENT_FONT_BOLD, expectedTextSettings.isBold());
        setCheckBox(CheckBoxId.TOURNAMENT_FONT_ITALIC, expectedTextSettings.isItalic());
        setCheckBox(CheckBoxId.TOURNAMENT_FONT_SHADOW, expectedTextSettings.isShadow());
        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_CONTOUR,
                expectedTextSettings.getContour());

        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_TOP_LEFT_OFFSET,
                expectedTextSettings.getDownOffsetTop()[0]);
        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_TOP_RIGHT_OFFSET,
                expectedTextSettings.getDownOffsetTop()[1]);
        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_BOTTOM_LEFT_OFFSET,
                expectedTextSettings.getDownOffsetBottom()[0]);
        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FONT_BOTTOM_RIGHT_OFFSET,
                expectedTextSettings.getDownOffsetBottom()[1]);

        selectInComboBox(ComboBoxId.TOURNAMENT_GAME, game.getName());

        scrollPaneVertically(ScrollPaneId.TOURNAMENT_SETTINGS, createScene, 1.0);
        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_FOREGROUND,
                expectedThumbnailSettings.getForeground());
        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_BACKGROUND,
                expectedThumbnailSettings.getBackground());
        writeInChosenJsonField(ChosenJsonFieldId.TOURNAMENT_THUMBNAIL_CHARACTER_SETTINGS,
                expectedThumbnailSettings.getFighterImageSettingsFile(artType));

        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_TOP8_FOREGROUND,
                expectedTop8Settings.getForeground());
        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_TOP8_BACKGROUND,
                expectedTop8Settings.getBackground());
        writeInChosenJsonField(ChosenJsonFieldId.TOURNAMENT_TOP8_SLOT_SETTINGS,
                expectedTop8Settings.getSlotSettingsFile());
        writeInChosenJsonField(ChosenJsonFieldId.TOURNAMENT_TOP8_CHARACTER_SETTINGS,
                expectedTop8Settings.getFighterImageSettingsFile(artType));

        WaitUtils.waitInSeconds(1);
        clickOnButton(ButtonId.SAVE_TOURNAMENT);

        WaitUtils.waitInSeconds(2);
        //Assert
        File actualTournamentSettings = FileUtils.loadTournamentsFile();
        FileUtils.assertSameTextFileContent(expectedTournamentFile, actualTournamentSettings);
        File actualTextSettings = FileUtils.loadTextFile();
        FileUtils.assertSameTextFileContent(expectedTextSettingsFile, actualTextSettings);
    }

    private static Stream<Arguments> getGamesAndDefaultArtTypeEnums() {
        return Stream.of(
                Arguments.of(Game.SSBU, SmashUltimateFighterArtType.RENDER)/*,
                Arguments.of(Game.ROA2, RivalsOfAether2FighterArtType.RENDER),
                Arguments.of(Game.SF6, StreetFighter6FighterArtType.RENDER),
                Arguments.of(Game.TEKKEN8, Tekken8FighterArtType.RENDER)*/
        );
    }
}
