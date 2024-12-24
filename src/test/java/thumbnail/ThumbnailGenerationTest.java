package thumbnail;

import crosscutting.CustomApplicationTest;
import crosscutting.PlayerInput;
import crosscutting.ThumbnailInput;
import enums.ButtonId;
import enums.CheckBoxId;
import enums.ComboBoxId;
import enums.SpinnerId;
import enums.TextFieldId;
import fighter.FighterArtType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import tournament.TournamentUtils;
import ui.controller.ThumbnailGeneratorController;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

public class ThumbnailGenerationTest extends CustomApplicationTest {

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
    public void testTournamentSelection() {
        verifyThat("#tournamentsLabel", hasText("Tournaments:"));
        clickOnButton(ButtonId.TOURNAMENT_WEEKLY_L);
        verifyThat("#tournamentsLabel", hasText("Tournament: Weekly L"));
    }

    @Test
    public void create_validThumbnailRenders_success() throws IOException {
        //Arrange
        ThumbnailInput input = generateThumbnailInput();
        File actualImage = new File(System.getProperty("user.dir") + "/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = new File(getClass().getResource("/expected/invictaMarioSonicThumbnail.png").getPath());

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
        input.setArtType(FighterArtType.MURAL);
        File actualImage = new File(System.getProperty("user.dir") + "/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = new File(getClass().getResource("/expected/invictaMuralMarioSonicThumbnail.png").getPath());

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
        File actualImage = new File(System.getProperty("user.dir") + "/generated_thumbnails/" + input.getExpectedFileName());
        File expectedImage = new File(getClass().getResource("/expected/invictaMarioSonicThumbnail.png").getPath());
        File marioImage = new File(System.getProperty("user.dir") + "/assets/fighters/mario/1.png");
        File sonicImage = new File(System.getProperty("user.dir") + "/assets/fighters/sonic/1.png");

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

        byte[] actualImageBytes = Files.readAllBytes(actualImage.toPath());
        byte[] expectedImageBytes = Files.readAllBytes(expectedImage.toPath());
        assertArrayEquals(expectedImageBytes, actualImageBytes);
        assertTrue(actualImage.delete());
        assertTrue(marioImage.delete());
        assertTrue(sonicImage.delete());
    }



    private void fillRoundData(ThumbnailInput input){
        writeInTextField(TextFieldId.ROUND, input.getRound());
        writeInTextField(TextFieldId.DATE, input.getDate());
        selectInComboBox(ComboBoxId.ART_TYPE, input.getArtType().getName());
    }

    private void fillPlayerData(PlayerInput input, String parentFxml){
        writeInTextField(parentFxml, TextFieldId.PLAYER, input.getPlayerName());
        writeAndSelectInComboBox(parentFxml,ComboBoxId.FIGHTER, input.getCharacterName());
        writeInSpinner(parentFxml, SpinnerId.FIGHTER_ALT, String.valueOf(input.getAlt()));
        setCheckBox(parentFxml, CheckBoxId.FLIP_FIGHTER, input.isFlip());
    }

    private ThumbnailInput generateThumbnailInput(){
        var players = List.of(
                new PlayerInput("Player 1", "Mario", 1, false),
                new PlayerInput("Player 2", "Sonic", 1, false)
        );
        return ThumbnailInput.builder()
                .tournamentId("weeklyl")
                .round("Winners Finals")
                .date("20/02/2022")
                .artType(FighterArtType.RENDER)
                .players(players)
                .build();
    }

}
