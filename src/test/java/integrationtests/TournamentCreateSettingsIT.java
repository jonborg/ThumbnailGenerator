package integrationtests;

import crosscutting.CustomApplicationTest;
import dto.CharacterInput;
import dto.PlayerInput;
import dto.ThumbnailInput;
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
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.service.TournamentService;
import utils.FileUtils;
import utils.TestUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void test_createTournament_success()
            throws InterruptedException, IOException {
        //Arrange
        Tournament expectedTournament = new Tournament(
                TestUtils.getTournament(tournamentService, "weeklyl"),
                "createITTest"
        );
        var game = Game.SSBU;
        var artType = SmashUltimateFighterArtTypeEnum.RENDER;
        var expectedThumbnailSettings = expectedTournament.getThumbnailSettingsByGame(game);
        var expectedTop8Settings = expectedTournament.getTop8SettingsByGame(game);
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
        //writeInComboBox(ComboBoxId.TOURNAMENT_THUMBNAIL_FONT, expectedTextSettings.getFont());
        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_LOGO, expectedTournament.getImage());

        /*
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
*/
        selectInComboBox(ComboBoxId.TOURNAMENT_GAME, game.getName());

        scrollPaneVertically(ScrollPaneId.TOURNAMENT_SETTINGS, createScene, 0.5);
        setCheckBox(CheckBoxId.CUSTOM_FOREGROUND, true);
        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_FOREGROUND,
                expectedThumbnailSettings.getThumbnailForeground().getForeground());
        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_BACKGROUND,
                expectedThumbnailSettings.getBackground());
        scrollPaneVertically(ScrollPaneId.TOURNAMENT_SETTINGS, createScene, 1.0);
        writeInChosenJsonField(ChosenJsonFieldId.TOURNAMENT_THUMBNAIL_CHARACTER_SETTINGS,
                expectedThumbnailSettings.getFighterImageSettingsFile(artType));

        scrollPaneVertically(ScrollPaneId.TOURNAMENT_SETTINGS, createScene, 1.0);
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

    //TODO
    public void test_createTournamentAndGenerateThumbnailWithDefaultForeground_success()
            throws InterruptedException, IOException {
        //Arrange
        Tournament expectedTournament = new Tournament(
                TestUtils.getTournament(tournamentService, "major_l"),
                "defaultForeground"
        );
        var thumbnailForegroundLogoPath = "assets/tournaments/logos/majorL.png";
        ThumbnailInput input = generateThumbnailInput();
        input.setArtType(SmashUltimateFighterArtTypeEnum.MURAL);
        File actualImage = FileUtils.getActualFile("/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = FileUtils.getFileFromResources(
                "/expected/thumbnail/invictaMuralMarioSonicThumbnail.png");

        //Act
        clickOnMenuOption(MenuId.EDIT);
        clickOnMenuOption(MenuId.CREATE_TOURNAMENT);

        WaitUtils.waitForWindowToLoad();
        Stage createStage = (Stage) getWindow(WindowId.CREATE_TOURNAMENT);
        Scene createScene = createStage.getScene();

        writeInTextField(TextFieldId.TOURNAMENT_NAME, expectedTournament.getName());
        writeInTextField(TextFieldId.TOURNAMENT_ID, expectedTournament.getTournamentId());

        scrollPaneVertically(ScrollPaneId.TOURNAMENT_SETTINGS, createScene, 0.5);
        writeInChosenImageField(ChosenImageFieldId.TOURNAMENT_THUMBNAIL_FOREGROUND_LOGO, thumbnailForegroundLogoPath);
        writeInTextField(TextFieldId.TOURNAMENT_THUMBNAIL_FOREGROUND_LOGO_SCALE, 0.35f);

        WaitUtils.waitInSeconds(1);
        clickOnButton(ButtonId.SAVE_TOURNAMENT);

        WaitUtils.waitInSeconds(2);

        //TODO scroll to new tournament and create static helper methods
        clickOnButton(ButtonId.TOURNAMENT_INVICTA);
        fillRoundData(input);
        fillPlayerData(input.getPlayers().get(0), "#player1");
        fillPlayerData(input.getPlayers().get(1), "#player2");

        //Act
        clickOnButton(ButtonId.SAVE_THUMBNAIL);

        //Assert
        boolean isFileCreated = WaitUtils.waitForExpectedFile(actualImage, expectedImage);

        assertTrue(isFileCreated);
        byte[] actualImageBytes = Files.readAllBytes(actualImage.toPath());
        byte[] expectedImageBytes = Files.readAllBytes(expectedImage.toPath());
        assertArrayEquals(expectedImageBytes, actualImageBytes);
        assertTrue(actualImage.delete());

    }

    private ThumbnailInput generateThumbnailInput(){
        var players = Arrays.asList(
                new PlayerInput("Player 1",
                        Collections.singletonList(new CharacterInput("Mario", 1, false))),
                new PlayerInput("Player 2",
                        Collections.singletonList(new CharacterInput("Sonic", 1, false)))
        );
        return ThumbnailInput.builder()
                .tournamentId("weeklyl")
                .round("Winners Finals")
                .date("20/02/2022")
                .artType(SmashUltimateFighterArtTypeEnum.RENDER)
                .players(players)
                .build();
    }
}
