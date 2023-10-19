package thumbnailgenerator.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;

@Getter
@Setter
@ToString
public class FileThumbnailSettings extends Settings implements Cloneable{

    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/default.json";
    private static String defaultMuralImageSettingsFile= "settings/thumbnails/images/defaultMural.json";

    private TextSettings textSettings;

    public FileThumbnailSettings(Game game, String foreground, String background,
                                 List<FighterArtSettings> artTypeDir, TextSettings textSettings){
        super(game, foreground, background, artTypeDir);
        this.textSettings = textSettings;
        this.artTypeDir.forEach(dir -> {
            if(dir.getFighterImageSettingsPath() == null
                    || dir.getFighterImageSettingsPath().isEmpty()) {
                switch (dir.getArtType()) {
                    case MURAL:
                        dir.setFighterImageSettingsPath(
                                defaultMuralImageSettingsFile);
                        break;
                    case RENDER:
                    default:
                        dir.setFighterImageSettingsPath(
                                defaultRenderImageSettingsFile);
                        break;
                }
            }
        });
    }

    @Override
    public String getFighterImageSettingsFile(
            SmashUltimateFighterArtType artType){
        if (this.getArtTypeDir() == null){
            switch (artType){
                case MURAL:
                    return defaultMuralImageSettingsFile;
                case RENDER:
                default:
                    return defaultRenderImageSettingsFile;
            }
        }
        return this.getArtTypeDir()
                .stream()
                .filter(dir -> artType.equals(dir.getArtType()))
                .findFirst()
                .get()
                .getFighterImageSettingsPath();
    }

    @Override
    public FileThumbnailSettings clone() {
        FileThumbnailSettings clone = null;
        try {
            clone = (FileThumbnailSettings) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
