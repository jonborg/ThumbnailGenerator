package thumbnail;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thumbnailgenerator.Main;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.LoadingType;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.service.ThumbnailService;
import thumbnailgenerator.service.TournamentService;
import thumbnailgenerator.ui.loading.LoadingState;
import utils.FileUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Main.class)
public class MultiThumbnailGenerationTest {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private ThumbnailService thumbnailService;

    @BeforeEach
    public void init(){
        JFXPanel fxPanel = new JFXPanel();
        tournamentService.initTournamentsListAndSettings();
    }

    @ParameterizedTest
    @ValueSource(strings = {"multiThumbnailGeneration.txt", "multiThumbnailGenerationNoArtType.txt"})
    public void create_validThumbnailFileRenders_success(String inputFile)
            throws IOException, FighterImageSettingsNotFoundException,
            InterruptedException {
        //Arrange
        var loadingState = new LoadingState(false, LoadingType.THUMBNAIL ,0 ,0);
        File thumbnailListFile = new File(getClass().getResource("/input/" + inputFile).getPath());
        List<File> expectedThumbnails = Arrays.asList(
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiThumbnail1.png"),
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiThumbnail2.png"),
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiThumbnail3.png")
        );
        List<File> actualThumbnails = Arrays.asList(
                FileUtils.getActualFile("/generated_thumbnails/UrQte-robin11--Arjay-byleth28--Losers-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/fizbo-pikachu4--Darkout-pokemon_trainer15--winners Semis-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/Chime-rob2--Toaster-marth5--Grand Final-20_02_20XX.png")
        );

        //Act
        InputStream fileInputsStream = new FileInputStream(thumbnailListFile);
        thumbnailService.generateAndSaveThumbnailsFromFile(fileInputsStream, false, loadingState);

        //Assert
        for (int i=0; i<3; i++) {
            var thumbnailExists = WaitUtils.waitForFile(actualThumbnails.get(i));
            if(i==0){
                WaitUtils.waitInSeconds(20);
            }
            assertTrue(thumbnailExists);
            assertArrayEquals(
                    Files.readAllBytes(expectedThumbnails.get(i).toPath()),
                    Files.readAllBytes(actualThumbnails.get(i).toPath())
            );
            assertTrue(actualThumbnails.get(i).delete());
        }
    }

    @Test
    public void create_validThumbnailFileMural_success()
            throws IOException, FighterImageSettingsNotFoundException,
            InterruptedException {
        //Arrange
        var loadingState = new LoadingState(false,LoadingType.THUMBNAIL ,0 ,0);
        File thumbnailListFile = new File(getClass().getResource("/input/multiMuralThumbnailGeneration.txt").getPath());
        List<File> expectedThumbnails = Arrays.asList(
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiMuralThumbnail1.png"),
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiMuralThumbnail2.png"),
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiMuralThumbnail3.png")
        );
        List<File> actualThumbnails = Arrays.asList(
                FileUtils.getActualFile("/generated_thumbnails/UrQte-robin11--Arjay-byleth28--Losers-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/fizbo-pikachu1--Darkout-pokemon_trainer15--winners Semis-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/Chime-rob2--Toaster-marth5--Grand Final-20_02_20XX.png")
        );

        //Act
        InputStream fileInputsStream = new FileInputStream(thumbnailListFile);
        thumbnailService.generateAndSaveThumbnailsFromFile(fileInputsStream, false, loadingState);

        //Assert
        for (int i=0; i<3; i++) {
            var thumbnailExists = WaitUtils.waitForFile(actualThumbnails.get(i));
            if(i==0){
                WaitUtils.waitInSeconds(20);
            }
            assertTrue(thumbnailExists);
            assertArrayEquals(
                    Files.readAllBytes(expectedThumbnails.get(i).toPath()),
                    Files.readAllBytes(actualThumbnails.get(i).toPath())
            );
            assertTrue(actualThumbnails.get(i).delete());
        }
    }

    @Test
    public void create_validThumbnailFileRendersSaveLocally_success()
            throws IOException, FighterImageSettingsNotFoundException,
            InterruptedException {
        //Arrange
        var loadingState = new LoadingState(false,LoadingType.THUMBNAIL ,0 ,0);
        File thumbnailListFile = new File(getClass().getResource("/input/multiThumbnailGeneration.txt").getPath());
        List<File> expectedThumbnails = Arrays.asList(
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiThumbnail1.png"),
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiThumbnail2.png"),
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiThumbnail3.png")
        );
        List<File> actualThumbnails = Arrays.asList(
                FileUtils.getActualFile("/generated_thumbnails/UrQte-robin11--Arjay-byleth28--Losers-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/fizbo-pikachu4--Darkout-pokemon_trainer15--winners Semis-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/Chime-rob2--Toaster-marth5--Grand Final-20_02_20XX.png")
        );
        List<File> characterImages = Arrays.asList(
                FileUtils.getCharacterImage(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER, "robin", 1),
                FileUtils.getCharacterImage(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER, "byleth", 8),
                FileUtils.getCharacterImage(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER, "pikachu", 4),
                FileUtils.getCharacterImage(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER, "pokemon_trainer", 5),
                FileUtils.getCharacterImage(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER, "rob", 2),
                FileUtils.getCharacterImage(Game.SSBU, SmashUltimateFighterArtTypeEnum.RENDER, "marth", 5)
        );

        //Act
        InputStream fileInputsStream = new FileInputStream(thumbnailListFile);
        thumbnailService.generateAndSaveThumbnailsFromFile(fileInputsStream, true, loadingState);

        //Assert
        for (int i=0; i<3; i++) {
            var thumbnailExists = WaitUtils.waitForFile(actualThumbnails.get(i));
            if(i==0){
                WaitUtils.waitInSeconds(20);
            }
            assertTrue(thumbnailExists);
            assertArrayEquals(
                    Files.readAllBytes(expectedThumbnails.get(i).toPath()),
                    Files.readAllBytes(actualThumbnails.get(i).toPath())
            );
            assertTrue(actualThumbnails.get(i).delete());
            assertTrue(characterImages.get(i*2).exists());
            assertTrue(characterImages.get(i*2 + 1).exists());
            assertTrue(characterImages.get(i*2).delete());
            assertTrue(characterImages.get(i*2 + 1).delete());
        }
    }

    @Test
    public void create_validThumbnailFileDoubleCharacterPerPlayer_success()
            throws IOException,
            FighterImageSettingsNotFoundException, InterruptedException {
        var loadingState = new LoadingState(false,LoadingType.THUMBNAIL ,0 ,0);
        File thumbnailListFile = new File(getClass().getResource("/input/multiThumbnailDoubleCharacterGeneration.txt").getPath());
        List<File> expectedThumbnails = Arrays.asList(
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiThumbnailDoubleCharacter1.png"),
                FileUtils.getFileFromResources(
                        "/expected/thumbnail/multiThumbnailDoubleCharacter2.png")
        );
        List<File> actualThumbnails = Arrays.asList(
                FileUtils.getActualFile("/generated_thumbnails/UrQte-robin11--Arjay-ganondorf5--Losers-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/Aegis-corrin28--Toaster-marth5--winners Semis-20_02_20XX.png")
        );

        //Act
        InputStream fileInputsStream = new FileInputStream(thumbnailListFile);
        thumbnailService.generateAndSaveThumbnailsFromFile(fileInputsStream, false, loadingState);

        //Assert
        for (int i=0; i<2; i++) {
            var thumbnailExists = WaitUtils.waitForFile(actualThumbnails.get(i));
            if(i==0){
                WaitUtils.waitInSeconds(20);
            }
            assertTrue(thumbnailExists);
            assertArrayEquals(
                    Files.readAllBytes(expectedThumbnails.get(i).toPath()),
                    Files.readAllBytes(actualThumbnails.get(i).toPath())
            );
            assertTrue(actualThumbnails.get(i).delete());
        }
    }
}
