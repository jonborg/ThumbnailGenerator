package thumbnailgenerator.utils.file;

import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.GeneratedGraphic;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;

public class FileUtils {

    private final static String tournamentFile = "settings/tournaments/tournaments.json";
    private final static String textSettingsFile = "settings/thumbnails/text/text.json";

    private final static String localRenderPath = "assets/characters/";
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

    public static String getLocalFightersPath(
            GeneratedGraphic generatedGraphic){
        if(Game.SF6.equals(generatedGraphic.getGame())){
            return getLocalRenderPath();
        } else {
            if (SmashUltimateFighterArtType.MURAL
                    .equals(generatedGraphic.getArtType())){
                return getLocalMuralPath();
            } else {
                return getLocalRenderPath();
            }
        }
    }
}
