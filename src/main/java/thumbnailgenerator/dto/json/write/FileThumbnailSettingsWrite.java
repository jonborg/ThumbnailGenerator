package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.TextSettings;

import java.util.List;
import java.util.stream.Collectors;

public class FileThumbnailSettingsWrite {
    @Expose
    @SerializedName("game")
    private String game;
    @Expose
    @SerializedName("foreground")
    private String foreground;
    @Expose
    @SerializedName("foregroundLogo")
    private ThumbnailForegroundLogoWrite foregroundLogo;
    @Expose
    @SerializedName("background")
    private String background;
    @Expose
    @SerializedName("artSettings")
    private List<FighterArtSettingsWrite> artTypeDir;

    private TextSettings textSettings;

    public FileThumbnailSettingsWrite(FileThumbnailSettings fileThumbnailSettings){
        this.game = fileThumbnailSettings.getGame().name();
        this.foreground = fileThumbnailSettings.getForeground();
        this.foregroundLogo = new ThumbnailForegroundLogoWrite(fileThumbnailSettings.getThumbnailForegroundLogo());
        this.background = fileThumbnailSettings.getBackground();
        this.artTypeDir = fileThumbnailSettings.getArtTypeDir()
                .stream()
                .map(FighterArtSettingsWrite::new)
                .collect(Collectors.toList());

    }
}
