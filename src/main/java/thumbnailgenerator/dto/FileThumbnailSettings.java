package thumbnailgenerator.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileThumbnailSettings extends Settings implements Cloneable{

    private ThumbnailForegroundLogo thumbnailForegroundLogo;
    private TextSettings textSettings;

    public FileThumbnailSettings(Game game, String foreground, ThumbnailForegroundLogo thumbnailForegroundLogo, String background,
                                 List<FighterArtSettings> artTypeDir, TextSettings textSettings){
        super(game, foreground, background, artTypeDir);
        this.thumbnailForegroundLogo = thumbnailForegroundLogo;
        this.textSettings = textSettings;
    }

    public FileThumbnailSettings(FileThumbnailSettings fileThumbnailSettings, String suffix){
        this(
               fileThumbnailSettings.getGame(),
               fileThumbnailSettings.getForeground(),
               fileThumbnailSettings.getThumbnailForegroundLogo(),
               fileThumbnailSettings.getBackground(),
               fileThumbnailSettings.getArtTypeDir()
                       .stream()
                       .map(f -> FighterArtSettings
                               .builder()
                               .artType(f.getArtType())
                               .fighterImageSettingsPath(f.getFighterImageSettingsPath())
                               .build()
                       )
                       .collect(Collectors.toList()),
                new TextSettings(fileThumbnailSettings.getTextSettings(), suffix)
        );
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
