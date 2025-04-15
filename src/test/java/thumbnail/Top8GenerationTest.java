package thumbnail;

import exception.Top8FromFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thumbnailgenerator.Main;
import thumbnailgenerator.service.Top8Service;
import thumbnailgenerator.service.TournamentUtils;
import utils.FileUtils;
import utils.WaitUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Main.class)
public class Top8GenerationTest {

    @BeforeEach
    public void init(){
        TournamentUtils.initTournamentsListAndSettings();
    }

    @Autowired
    private Top8Service top8Service;

    @Test
    public void create_validTop8_success()
            throws IOException, Top8FromFileException {
        //Arrange
        File top8File = new File(getClass().getResource("/input/top8Generation.txt").getPath());
        File expectedTop8 = FileUtils.getFileFromResources(
                "/expected/top8/multiTop8Generation.png");

        //Act
        InputStream fileInputsStream = new FileInputStream(top8File);
        top8Service.generateTop8FromFile(fileInputsStream, false);

        //Assert
        File actualTop8 = FileUtils.getMostRecentFile("/generated_top8/");
        var thumbnailExists = WaitUtils.waitForFile(actualTop8);
        assertTrue(thumbnailExists);
        assertArrayEquals(
                Files.readAllBytes(expectedTop8.toPath()),
                Files.readAllBytes(actualTop8.toPath())
        );
        assertTrue(actualTop8.delete());
    }
}
