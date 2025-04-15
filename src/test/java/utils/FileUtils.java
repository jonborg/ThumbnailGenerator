package utils;

import thumbnailgenerator.dto.Game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    public static File getCharacterImage(Game game, String characterUrlName, int alt){
        return FileUtils.getActualFile("/assets/characters/" + game.toString() + "/" + characterUrlName + "/" + alt + ".png");
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
}
