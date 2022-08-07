package file;

import fighter.FighterArtType;

public class FileUtils {

    private final static String tournamentFile = "settings/tournaments/tournaments.json";
    private final static String textSettingsFile = "settings/thumbnails/text/text.json";

    private final static String localRenderPath = "assets/fighters/";
    private final static String localMuralPath = "assets/mural/";
    private final static String saveThumbnailsPath = "generated_thumbnails/";


    public static String getTournamentFile() {
        return tournamentFile;
    }

    public static String getTextSettingsFile() {
        return textSettingsFile;
    }

    public static String getLocalRenderPath() {
        return localRenderPath;
    }

    public static String getLocalMuralPath() {
        return localMuralPath;
    }

    public static String getSaveThumbnailsPath() {
        return saveThumbnailsPath;
    }

    public static String getLocalFightersPath(FighterArtType artType){
        switch(artType){
            case MURAL:
                return getLocalMuralPath();
            case RENDER:
            default:
                return  getLocalRenderPath();
        }
    }
}
