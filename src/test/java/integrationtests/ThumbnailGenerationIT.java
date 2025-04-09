package integrationtests;

import crosscutting.CustomApplicationTest;
import dto.PlayerInput;
import dto.ThumbnailInput;
import enums.ButtonId;
import enums.CheckBoxId;
import enums.ComboBoxId;
import enums.SpinnerId;
import enums.TextFieldId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import thumbnailgenerator.Main;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import utils.FileUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

@SpringBootTest(classes = Main.class)
public class ThumbnailGenerationIT extends CustomApplicationTest {

    @Test
    public void testTournamentSelection() {
        verifyThat("#tournamentsLabel", hasText("Tournaments:"));
        clickOnButton(ButtonId.TOURNAMENT_WEEKLY_L);
        verifyThat("#tournamentsLabel", hasText("Tournament: Weekly L"));
    }

    @Test
    public void create_validThumbnailRenders_success() throws IOException {
        //Arrange
        ThumbnailInput input = generateThumbnailInput();
        File actualImage = FileUtils.getActualFile("/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = FileUtils.getFileFromResources(
                "/expected/thumbnail/invictaMarioSonicThumbnail.png");

        clickOnButton(ButtonId.TOURNAMENT_INVICTA);
        fillRoundData(input);
        fillPlayerData(input.getPlayers().get(0), "#player1");
        fillPlayerData(input.getPlayers().get(1), "#player2");
        setCheckBox(CheckBoxId.SAVE_LOCALLY, false);

        //Act
        clickOnButton(ButtonId.SAVE_THUMBNAIL);

        //Assert
        boolean isFileCreated = WaitUtils.waitForFile(actualImage);
        assertTrue(isFileCreated);

        byte[] actualImageBytes = Files.readAllBytes(actualImage.toPath());
        byte[] expectedImageBytes = Files.readAllBytes(expectedImage.toPath());
        assertArrayEquals(expectedImageBytes, actualImageBytes);
        assertTrue(actualImage.delete());
    }

    @Test
    public void create_validThumbnailMuralArts_success() throws IOException {
        //Arrange
        ThumbnailInput input = generateThumbnailInput();
        input.setArtType(SmashUltimateFighterArtType.MURAL);
        File actualImage = FileUtils.getActualFile("/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = FileUtils.getFileFromResources(
                "/expected/thumbnail/invictaMuralMarioSonicThumbnail.png");

        clickOnButton(ButtonId.TOURNAMENT_INVICTA);
        fillRoundData(input);
        fillPlayerData(input.getPlayers().get(0), "#player1");
        fillPlayerData(input.getPlayers().get(1), "#player2");
        setCheckBox(CheckBoxId.SAVE_LOCALLY, false);

        //Act
        clickOnButton(ButtonId.SAVE_THUMBNAIL);

        //Assert
        boolean isFileCreated = WaitUtils.waitForFile(actualImage);
        assertTrue(isFileCreated);

        byte[] actualImageBytes = Files.readAllBytes(actualImage.toPath());
        byte[] expectedImageBytes = Files.readAllBytes(expectedImage.toPath());
        assertArrayEquals(expectedImageBytes, actualImageBytes);
        assertTrue(actualImage.delete());
    }

    @Test
    public void create_validThumbnailSaveLocally_success() throws IOException {
        //Arrange
        ThumbnailInput input = generateThumbnailInput();
        File actualImage = FileUtils.getActualFile("/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = FileUtils.getFileFromResources(
                "/expected/thumbnail/invictaMarioSonicThumbnail.png");
        File marioImage = FileUtils.getCharacterImage(Game.SSBU, "mario", 1);
        File sonicImage = FileUtils.getCharacterImage(Game.SSBU, "sonic", 1);

        clickOnButton(ButtonId.TOURNAMENT_INVICTA);
        fillRoundData(input);
        fillPlayerData(input.getPlayers().get(0), "#player1");
        fillPlayerData(input.getPlayers().get(1), "#player2");
        setCheckBox(CheckBoxId.SAVE_LOCALLY, true);

        //Act
        clickOnButton(ButtonId.SAVE_THUMBNAIL);

        //Assert
        boolean isFileCreated = WaitUtils.waitForFile(actualImage);
        assertTrue(isFileCreated);
        assertTrue(marioImage.exists());
        assertTrue(sonicImage.exists());
        assertArrayEquals(
                Files.readAllBytes(expectedImage.toPath()),
                Files.readAllBytes(actualImage.toPath())
        );
        assertTrue(actualImage.delete());
        assertTrue(marioImage.delete());
        assertTrue(sonicImage.delete());
    }

    private void fillRoundData(ThumbnailInput input){
        writeInTextField(TextFieldId.ROUND, input.getRound());
        writeInTextField(TextFieldId.DATE, input.getDate());
        selectInComboBox(ComboBoxId.ART_TYPE, ((SmashUltimateFighterArtType) input.getArtType()).getName());
    }

    private void fillPlayerData(PlayerInput input, String parentFxml){
        writeInTextField(parentFxml, TextFieldId.PLAYER, input.getPlayerName());
        writeAndSelectInComboBox(parentFxml,ComboBoxId.FIGHTER, input.getCharacterName());
        writeInSpinner(parentFxml, SpinnerId.FIGHTER_ALT, String.valueOf(input.getAlt()));
        setCheckBox(parentFxml, CheckBoxId.FLIP_FIGHTER, input.isFlip());
    }

    private ThumbnailInput generateThumbnailInput(){
        var players = Arrays.asList(
                new PlayerInput("Player 1", "Mario", 1, false),
                new PlayerInput("Player 2", "Sonic", 1, false)
        );
        return ThumbnailInput.builder()
                .tournamentId("weeklyl")
                .round("Winners Finals")
                .date("20/02/2022")
                .artType(SmashUltimateFighterArtType.RENDER)
                .players(players)
                .build();
    }
}
