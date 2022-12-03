package tournament.settings;

import fighter.FighterArtType;
import fighter.image.settings.FighterArtSettings;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import thumbnail.text.TextSettings;

@Getter
@Setter
@ToString
public class ThumbnailSettings extends Settings {

    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/default.json";
    private static String defaultMuralImageSettingsFile= "settings/thumbnails/images/defaultMural.json";

    private TextSettings textSettings;

    public ThumbnailSettings (String foreground, String background,
                              List<FighterArtSettings> artTypeDir, TextSettings textSettings){
        super(foreground, background, artTypeDir);
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
    public String getFighterImageSettingsFile(FighterArtType artType){
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
}
