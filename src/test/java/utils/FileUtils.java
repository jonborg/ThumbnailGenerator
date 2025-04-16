package utils;

import org.junit.Assert;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.interfaces.FighterArtType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtils {

    private FileUtils(){
        throw new UnsupportedOperationException("Utils class");
    }

    public static File getFileFromResources(String path){
        return new File(FileUtils.class.getResource(path).getPath());
    }

    public static File getActualFile(String path) {
        return new File(System.getProperty("user.dir") + path);
    }

    public static File getCharacterImage(Game game, FighterArtType artType, String characterUrlName, int alt){
        return FileUtils.getActualFile("/assets/characters/"
                + game.toString() + "/"
                + artType.getEnumName().toLowerCase() + "/"
                + characterUrlName + "/"
                + alt + ".png");
    }

    public static File getMostRecentFile(String folderPath){
        return Arrays.stream(
                Objects.requireNonNull(getActualFile(folderPath).listFiles()))
                .filter(f -> !f.isDirectory())
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);
    }

    public static void assertSameFileContent(File expected, File actual)
            throws IOException {
        assertTrue(
                Arrays.equals(
                        Files.readAllBytes(expected.toPath()),
                        Files.readAllBytes(actual.toPath())
                )
        );
    }

    public static void assertSameTextFileContent(File expected, File actual)
            throws IOException {
        Assert.assertEquals(
                normalizeLineEndings(Files.readString(expected.toPath())),
                normalizeLineEndings(Files.readString(actual.toPath()))
        );
    }

    public static File loadTournamentsFile(){
        return getActualFile("/settings/tournaments/tournaments.json");
    }

    public static File loadTextFile(){
        return getActualFile("/settings/thumbnails/text/text.json");
    }

    public static void createFileBackups() throws IOException {
        File originalTournamentPath = loadTournamentsFile();
        File backupTournamentPath = getActualFile("/settings/tournaments/tournamentsBackup.json");
        Files.copy(
                originalTournamentPath.toPath(),
                backupTournamentPath.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        File originalTextPath = loadTextFile();
        File backupTextPath = getActualFile("/settings/thumbnails/text/textBackup.json");
        Files.copy(
                originalTextPath.toPath(),
                backupTextPath.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

    }

    public static void loadFileBackups() throws IOException {
        File originalTournamentPath = loadTournamentsFile();
        File backupTournamentPath = getActualFile("/settings/tournaments/tournamentsBackup.json");

        Files.copy(
                backupTournamentPath.toPath(),
                originalTournamentPath.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        File originalTextPath = loadTextFile();
        File backupTextPath = getActualFile("/settings/thumbnails/text/textBackup.json");
        Files.copy(
                backupTextPath.toPath(),
                originalTextPath.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        Files.deleteIfExists(backupTextPath.toPath());
    }

    private static String normalizeLineEndings(String content) {
        return content.replace("\r\n", "\n").trim();
    }
}
