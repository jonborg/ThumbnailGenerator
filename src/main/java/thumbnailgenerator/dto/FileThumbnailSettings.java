package thumbnailgenerator.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import thumbnailgenerator.dto.json.read.FileThumbnailSettingsRead;
import thumbnailgenerator.enums.interfaces.FighterArtType;
import thumbnailgenerator.utils.enums.FighterArtTypeUtils;

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

    public FileThumbnailSettings(FileThumbnailSettingsRead fileThumbnailSettingsRead){
        this(
                Game.valueOf(fileThumbnailSettingsRead.getGame()),
                fileThumbnailSettingsRead.getForeground(),
                fileThumbnailSettingsRead.getBackground(),
                FighterArtTypeUtils.convertArtSettings(fileThumbnailSettingsRead.getArtTypeDir(),Game.valueOf(fileThumbnailSettingsRead.getGame())),
                new TextSettings((String) null)
        );
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
