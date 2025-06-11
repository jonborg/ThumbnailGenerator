package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.ThumbnailForeground;
import thumbnailgenerator.dto.ThumbnailForegroundLogo;

import java.util.Map;

@Getter
@Setter
public class ThumbnailForegroundWrite {
    @Expose
    @SerializedName("foreground")
    private String foreground;
    @Expose
    @SerializedName("colors")
    private Map<String, String> colors;
    @Expose
    @SerializedName("foregroundLogo")
    private ThumbnailForegroundLogoWrite thumbnailForegroundLogo;
    @Expose
    @SerializedName("customForeground")
    private boolean customForeground;

    public ThumbnailForegroundWrite(ThumbnailForeground thumbnailForeground){
        this.foreground = thumbnailForeground.getForeground();
        this.colors = thumbnailForeground.getColors();
        this.thumbnailForegroundLogo = new ThumbnailForegroundLogoWrite(thumbnailForeground.getThumbnailForegroundLogo());
        this.customForeground = thumbnailForeground.isCustomForeground();
    }
}
