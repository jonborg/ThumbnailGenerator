package integrationtests;

import crosscutting.CustomApplicationTest;
import dto.CharacterInput;
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
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import utils.FileUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

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
    public void create_validThumbnailRenders_success()
            throws IOException, InterruptedException {
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
        WaitUtils.waitInSeconds(2);

        assertTrue(isFileCreated);
        byte[] actualImageBytes = Files.readAllBytes(actualImage.toPath());
        byte[] expectedImageBytes = Files.readAllBytes(expectedImage.toPath());
        assertArrayEquals(expectedImageBytes, actualImageBytes);
        assertTrue(actualImage.delete());
    }

    @Test
    public void create_validThumbnailMuralArts_success()
            throws IOException, InterruptedException {
        //Arrange
        ThumbnailInput input = generateThumbnailInput();
        input.setArtType(SmashUltimateFighterArtTypeEnum.MURAL);
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
        WaitUtils.waitInSeconds(2);

        assertTrue(isFileCreated);
        byte[] actualImageBytes = Files.readAllBytes(actualImage.toPath());
        byte[] expectedImageBytes = Files.readAllBytes(expectedImage.toPath());
        assertArrayEquals(expectedImageBytes, actualImageBytes);
        assertTrue(actualImage.delete());
    }

    @Test
    public void create_validThumbnailSaveLocally_success()
            throws IOException, InterruptedException {
        //Arrange
        ThumbnailInput input = generateThumbnailInput();
        File actualImage = FileUtils.getActualFile("/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = FileUtils.getFileFromResources(
                "/expected/thumbnail/invictaMarioSonicThumbnail.png");
        File marioImage = FileUtils.getCharacterImage(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER, "mario", 1);
        File sonicImage = FileUtils.getCharacterImage(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER, "sonic", 1);

        clickOnButton(ButtonId.TOURNAMENT_INVICTA);
        fillRoundData(input);
        fillPlayerData(input.getPlayers().get(0), "#player1");
        fillPlayerData(input.getPlayers().get(1), "#player2");
        setCheckBox(CheckBoxId.SAVE_LOCALLY, true);

        //Act
        clickOnButton(ButtonId.SAVE_THUMBNAIL);

        //Assert
        boolean isFileCreated = WaitUtils.waitForFile(actualImage);
        WaitUtils.waitInSeconds(2);

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

    @Test
    public void create_validThumbnailDoubleCharacters_success()
            throws IOException, InterruptedException {
        //Arrange
        ThumbnailInput input = generateThumbnailDoubleCharactersInput();
        File actualImage = FileUtils.getActualFile("/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = FileUtils.getFileFromResources(
                "/expected/thumbnail/invictaMarioSonicDoubleCharacterThumbnail.png");

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
        WaitUtils.waitInSeconds(2);

        byte[] actualImageBytes = Files.readAllBytes(actualImage.toPath());
        byte[] expectedImageBytes = Files.readAllBytes(expectedImage.toPath());
        assertArrayEquals(expectedImageBytes, actualImageBytes);
        assertTrue(actualImage.delete());
    }


    private void fillRoundData(ThumbnailInput input){
        writeInTextField(TextFieldId.ROUND, input.getRound());
        writeInTextField(TextFieldId.DATE, input.getDate());
        selectInComboBox(ComboBoxId.ART_TYPE, ((SmashUltimateFighterArtTypeEnum) input.getArtType()).getValue());
    }

    private void fillPlayerData(PlayerInput input, String parentFxml){
        var character1 = input.getCharacterInputList().get(0);
        writeInTextField(parentFxml, TextFieldId.PLAYER, input.getPlayerName());
        writeAndSelectInComboBox(parentFxml,ComboBoxId.CHARACTER_1, character1.getCharacterName());
        writeInSpinner(parentFxml, SpinnerId.ALT_CHARACTER_1, String.valueOf(character1.getAlt()));
        setCheckBox(parentFxml, CheckBoxId.FLIP_CHARACTER_1, character1.isFlip());
        if (input.getCharacterInputList().size() == 2){
            var character2 = input.getCharacterInputList().get(1);
            clickOnButton(parentFxml, ButtonId.ADD_REMOVE_CHARACTER_2);
            writeAndSelectInComboBox(parentFxml,ComboBoxId.CHARACTER_2, character2.getCharacterName());
            writeInSpinner(parentFxml, SpinnerId.ALT_CHARACTER_2, String.valueOf(character1.getAlt()));
            setCheckBox(parentFxml, CheckBoxId.FLIP_CHARACTER_2, character1.isFlip());
        }
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

    private ThumbnailInput generateThumbnailDoubleCharactersInput(){
        var players = Arrays.asList(
                new PlayerInput("Player 1",
                        Arrays.asList(
                                new CharacterInput("Mario", 1, false),
                                new CharacterInput("Luigi", 1, false)
                            )
                ),
                new PlayerInput("Player 2",
                        Arrays.asList(
                                new CharacterInput("Sonic", 1, false),
                                new CharacterInput("Pac-Man", 1, false)
                        )
                )
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
