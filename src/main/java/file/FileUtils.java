package file;

import tournament.TournamentUtils;

public class FileUtils {

    private final static String tournamentFile = "settings/tournaments/tournaments.json";
    private final static String textSettingsFile = "settings/thumbnails/text/text.json";

    private final static String scaleFile = "settings/thumbnails/images/scale.txt";
    private final static String offsetFile = "settings/thumbnails/images/offset.txt";
    private final static String flipFile = "settings/thumbnails/images/flip.txt";

    private final static String localFightersPath = "assets/fighters/";
    private final static String saveThumbnailsPath = "generated_thumbnails/";


    public static String getTournamentFile() {
        return tournamentFile;
    }

    public static String getTextSettingsFile() {
        return textSettingsFile;
    }

    public static String getScaleFile() {
        return scaleFile;
    }

    public static String getOffsetFile() {
        return offsetFile;
    }

    public static String getFlipFile() {
        return flipFile;
    }

    public static String getLocalFightersPath() {
        return localFightersPath;
    }

    public static String getSaveThumbnailsPath() {
        return saveThumbnailsPath;
    }

}
