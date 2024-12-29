package thumbnail;

import exception.Top8FromFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top8.generate.Top8FromFile;
import tournament.TournamentUtils;
import utils.FileUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Top8GenerationTest {

    @BeforeEach
    public void init(){
        TournamentUtils.initTournamentsListAndSettings();
    }

    @Test
    public void create_validTop8_success()
            throws IOException, Top8FromFileException {
        //Arrange
        File top8File = new File(getClass().getResource("/input/top8Generation.txt").getPath());
        File expectedTop8 = FileUtils.getFileFromResources("/expected/multiTop8Generation.png");
        File actualTop8 = FileUtils.getMostRecentFile("/generated_top8/");

        //Act
        Top8FromFile.generateFromFile(top8File, false);

        //Assert
        var thumbnailExists = WaitUtils.waitForFile(actualTop8);
        assertTrue(thumbnailExists);
        assertArrayEquals(
                Files.readAllBytes(expectedTop8.toPath()),
                Files.readAllBytes(actualTop8.toPath())
        );
        assertTrue(actualTop8.delete());
    }
}
