package thumbnail;

import exception.Top8FromFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thumbnailgenerator.Main;
import thumbnailgenerator.service.Top8Service;
import thumbnailgenerator.service.TournamentService;
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

    @Autowired
    private TournamentService tournamentService;

    @BeforeEach
    public void init(){
        tournamentService.initTournamentsListAndSettings();
    }

    @Autowired
    private Top8Service top8Service;

    @ParameterizedTest
    @ValueSource(strings = {"top8Generation.txt", "top8GenerationNoArtType.txt"})
    public void create_validTop8_success(String inputFile)
            throws IOException, Top8FromFileException {
        //Arrange
        File top8File = new File(getClass().getResource("/input/" + inputFile).getPath());
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
