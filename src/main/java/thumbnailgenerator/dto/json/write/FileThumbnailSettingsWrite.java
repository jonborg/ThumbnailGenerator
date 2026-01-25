package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.TextSettings;

import java.util.List;
import java.util.stream.Collectors;

public class FileThumbnailSettingsWrite {
    @Expose
    @SerializedName("foreground")
    private ThumbnailForegroundWrite foreground;
    @Expose
    @SerializedName("background")
    private String background;
    @Expose
    @SerializedName("artSettings")
    private List<FighterArtSettingsWrite> artTypeDir;
    @Expose
    @SerializedName("textSettings")
    private TextSettingsWrite textSettings;

    public FileThumbnailSettingsWrite(FileThumbnailSettings fileThumbnailSettings){
        this.foreground = new ThumbnailForegroundWrite(fileThumbnailSettings.getThumbnailForeground());
        this.background = fileThumbnailSettings.getBackground();
        this.artTypeDir = fileThumbnailSettings.getArtTypeDir()
                .stream()
                .map(FighterArtSettingsWrite::new)
                .collect(Collectors.toList());
        this.textSettings = new TextSettingsWrite(fileThumbnailSettings.getTextSettings());
    }
}
