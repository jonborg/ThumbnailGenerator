package thumbnail;

import exception.FontNotFoundException;
import exception.ThumbnailFromFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thumbnail.generate.ThumbnailFromFile;
import tournament.TournamentUtils;
import utils.FileUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultiThumbnailGenerationTest {

    @BeforeEach
    public void init(){
        TournamentUtils.initTournamentsListAndSettings();
    }

    @Test
    public void create_validThumbnailFileRenders_success()
            throws ThumbnailFromFileException, FontNotFoundException,
            IOException {
        //Arrange
        File thumbnailListFile = new File(getClass().getResource("/input/multiThumbnailGeneration.txt").getPath());
        List<File> expectedThumbnails = List.of(
                FileUtils.getFileFromResources("/expected/multiThumbnail1.png"),
                FileUtils.getFileFromResources("/expected/multiThumbnail2.png"),
                FileUtils.getFileFromResources("/expected/multiThumbnail3.png")
        );
        List<File> actualThumbnails = List.of(
                FileUtils.getActualFile("/generated_thumbnails/UrQte-robin11--Arjay-byleth28--Losers-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/fizbo-pikachu4--Darkout-pokemon_trainer15--winners Semis-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/Chime-rob2--Toaster-marth5--Grand Final-20_02_20XX.png")
        );

        //Act
        ThumbnailFromFile.generateFromFile(thumbnailListFile, false);

        //Assert
        for (int i=0; i<3; i++) {
            var thumbnailExists = WaitUtils.waitForFile(actualThumbnails.get(i));
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
            throws ThumbnailFromFileException, FontNotFoundException,
            IOException {
        //Arrange
        File thumbnailListFile = new File(getClass().getResource("/input/multiMuralThumbnailGeneration.txt").getPath());
        List<File> expectedThumbnails = List.of(
                FileUtils.getFileFromResources("/expected/multiMuralThumbnail1.png"),
                FileUtils.getFileFromResources("/expected/multiMuralThumbnail2.png"),
                FileUtils.getFileFromResources("/expected/multiMuralThumbnail3.png")
        );
        List<File> actualThumbnails = List.of(
                FileUtils.getActualFile("/generated_thumbnails/UrQte-robin11--Arjay-byleth28--Losers-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/fizbo-pikachu1--Darkout-pokemon_trainer15--winners Semis-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/Chime-rob2--Toaster-marth5--Grand Final-20_02_20XX.png")
        );

        //Act
        ThumbnailFromFile.generateFromFile(thumbnailListFile, false);

        //Assert
        for (int i=0; i<3; i++) {
            var thumbnailExists = WaitUtils.waitForFile(actualThumbnails.get(i));
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
            throws ThumbnailFromFileException, FontNotFoundException,
            IOException {
        //Arrange
        File thumbnailListFile = new File(getClass().getResource("/input/multiThumbnailGeneration.txt").getPath());
        List<File> expectedThumbnails = List.of(
                FileUtils.getFileFromResources("/expected/multiThumbnail1.png"),
                FileUtils.getFileFromResources("/expected/multiThumbnail2.png"),
                FileUtils.getFileFromResources("/expected/multiThumbnail3.png")
        );
        List<File> actualThumbnails = List.of(
                FileUtils.getActualFile("/generated_thumbnails/UrQte-robin11--Arjay-byleth28--Losers-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/fizbo-pikachu4--Darkout-pokemon_trainer15--winners Semis-20_02_20XX.png"),
                FileUtils.getActualFile("/generated_thumbnails/Chime-rob2--Toaster-marth5--Grand Final-20_02_20XX.png")
        );
        List<File> characterImages = List.of(
                FileUtils.getCharacterImage("robin", 1),
                FileUtils.getCharacterImage("byleth", 8),
                FileUtils.getCharacterImage("pikachu", 4),
                FileUtils.getCharacterImage("pokemon_trainer", 5),
                FileUtils.getCharacterImage("rob", 2),
                FileUtils.getCharacterImage("marth", 5)
        );

        //Act
        ThumbnailFromFile.generateFromFile(thumbnailListFile, true);

        //Assert
        for (int i=0; i<3; i++) {
            var thumbnailExists = WaitUtils.waitForFile(actualThumbnails.get(i));
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
}
