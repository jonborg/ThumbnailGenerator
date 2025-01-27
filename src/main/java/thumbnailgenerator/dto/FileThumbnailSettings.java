package thumbnailgenerator.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
@Setter
@ToString
public class FileThumbnailSettings extends Settings implements Cloneable{

    private TextSettings textSettings;

    public FileThumbnailSettings(Game game, String foreground, String background,
                                 List<FighterArtSettings> artTypeDir, TextSettings textSettings){
        super(game, foreground, background, artTypeDir);
        this.textSettings = textSettings;
        this.artTypeDir.forEach(dir -> {
            if(dir.getFighterImageSettingsPath() == null
                    || dir.getFighterImageSettingsPath().isEmpty()) {
                String defaultFighterImageSettingsFile = dir
                        .getArtType()
                        .getDefaultFighterImageSettingsFile();
                dir.setFighterImageSettingsPath(defaultFighterImageSettingsFile);
            }
        });
    }

    @Override
    public String getFighterImageSettingsFile(
            FighterArtType artType){
        if (this.getArtTypeDir() == null){
            return artType.getDefaultFighterImageSettingsFile();
        } else {
            return super.getFighterImageSettingsFile(artType);
        }
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
