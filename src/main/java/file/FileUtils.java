package file;

public class FileUtils {

    private final static String tournamentFile = "settings/tournaments/tournaments.json";
    private final static String textSettingsFile = "settings/thumbnails/text/text.json";

    private final static String localFightersPath = "assets/fighters/";
    private final static String saveThumbnailsPath = "generated_thumbnails/";


    public static String getTournamentFile() {
        return tournamentFile;
    }

    public static String getTextSettingsFile() {
        return textSettingsFile;
    }

    public static String getLocalFightersPath() {
        return localFightersPath;
    }

    public static String getSaveThumbnailsPath() {
        return saveThumbnailsPath;
    }

}
