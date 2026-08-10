package integrationtests;

import crosscutting.CustomApplicationTest;
import dto.CharacterInput;
import dto.PlayerInput;
import dto.ThumbnailInput;
import enums.ButtonId;
import enums.CheckBoxId;
import enums.ComboBoxId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import thumbnailgenerator.Main;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.games.ssbu.SmashUltimateFighterArtTypeEnum;
import utils.FileUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

@SpringBootTest(classes = Main.class)
public class ThumbnailGenerationIT extends CustomApplicationTest {

    @Test
    public void testTournamentSelection() {
        verifyThat("#tournamentsLabel", hasText("Tournament:"));
        clickOnButton(ButtonId.TOURNAMENT_WEEKLY_L);
        verifyThat("#tournamentsLabel", hasText("Tournament: Weekly L"));
    }

    @Test
    public void testAddingAndRemovingMultipleCharacters() {
        clickOnButton("#player1", ButtonId.ADD_REMOVE_CHARACTER_2);
        clickOnButton("#player1", ButtonId.ADD_REMOVE_CHARACTER_3);
        clickOnButton("#player1", ButtonId.ADD_REMOVE_CHARACTER_4);

        assertNotNull(findElement(ComboBoxId.CHARACTER_1.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_2.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_3.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_4.getValue(), "#player1"));
        assertNull(findElement(ComboBoxId.CHARACTER_5.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_1.getValue(), "#player2"));
        assertNull(findElement(ComboBoxId.CHARACTER_2.getValue(), "#player2"));
        assertNull(findElement(ComboBoxId.CHARACTER_3.getValue(), "#player2"));
        assertNull(findElement(ComboBoxId.CHARACTER_4.getValue(), "#player2"));
        assertNull(findElement(ComboBoxId.CHARACTER_5.getValue(), "#player2"));

        clickOnButton("#player2", ButtonId.ADD_REMOVE_CHARACTER_2);
        clickOnButton("#player2", ButtonId.ADD_REMOVE_CHARACTER_3);
        clickOnButton("#player2", ButtonId.ADD_REMOVE_CHARACTER_4);
        clickOnButton("#player2", ButtonId.ADD_REMOVE_CHARACTER_5);

        assertNotNull(findElement(ComboBoxId.CHARACTER_1.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_2.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_3.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_4.getValue(), "#player1"));
        assertNull(findElement(ComboBoxId.CHARACTER_5.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_1.getValue(), "#player2"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_2.getValue(), "#player2"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_3.getValue(), "#player2"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_4.getValue(), "#player2"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_5.getValue(), "#player2"));

        clickOnButton("#player1", ButtonId.ADD_REMOVE_CHARACTER_3);
        clickOnButton("#player2", ButtonId.ADD_REMOVE_CHARACTER_5);
        assertNotNull(findElement(ComboBoxId.CHARACTER_1.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_2.getValue(), "#player1"));
        assertNull(findElement(ComboBoxId.CHARACTER_3.getValue(), "#player1"));
        assertNull(findElement(ComboBoxId.CHARACTER_4.getValue(), "#player1"));
        assertNull(findElement(ComboBoxId.CHARACTER_5.getValue(), "#player1"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_1.getValue(), "#player2"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_2.getValue(), "#player2"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_3.getValue(), "#player2"));
        assertNotNull(findElement(ComboBoxId.CHARACTER_4.getValue(), "#player2"));
        assertNull(findElement(ComboBoxId.CHARACTER_5.getValue(), "#player2"));
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
        boolean isFileCreated = WaitUtils.waitForExpectedFile(actualImage, expectedImage);
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
        boolean isFileCreated = WaitUtils.waitForExpectedFile(actualImage, expectedImage);

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
        boolean isFileCreated = WaitUtils.waitForExpectedFile(actualImage, expectedImage);

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
    public void create_validThumbnailMultiCharacters_success()
            throws IOException, InterruptedException {
        //Arrange
        ThumbnailInput input = generateThumbnailDoubleCharactersInput();
        File actualImage = FileUtils.getActualFile("/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = FileUtils.getFileFromResources(
                "/expected/thumbnail/invictaMarioSonicMultiCharacterThumbnail.png");

        clickOnButton(ButtonId.TOURNAMENT_INVICTA);
        fillRoundData(input);
        fillPlayerData(input.getPlayers().get(0), "#player1");
        fillPlayerData(input.getPlayers().get(1), "#player2");
        setCheckBox(CheckBoxId.SAVE_LOCALLY, false);

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

    private ThumbnailInput generateThumbnailDoubleCharactersInput(){
        var players = Arrays.asList(
                new PlayerInput("Player 1",
                        Arrays.asList(
                                new CharacterInput("Mario", 1, false),
                                new CharacterInput("Luigi", 1, false),
                                new CharacterInput("Wario", 1, false)
                            )
                ),
                new PlayerInput("Player 2",
                        Arrays.asList(
                                new CharacterInput("Sonic", 1, false),
                                new CharacterInput("Snake", 1, false),
                                new CharacterInput("Mega Man", 1, false),
                                new CharacterInput("Pac-Man", 1, false),
                                new CharacterInput("Ryu", 1, false)
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
