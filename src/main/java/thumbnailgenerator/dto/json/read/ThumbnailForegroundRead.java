package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.ThumbnailForeground;
import thumbnailgenerator.dto.ThumbnailForegroundVersus;
import thumbnailgenerator.dto.json.write.ThumbnailForegroundLogoWrite;

import java.util.Map;

@Getter
@Setter
public class ThumbnailForegroundRead {
    @Expose
    @SerializedName("foregroundImage")
    private String foreground;
    @Expose
    @SerializedName("colors")
    private Map<String, String> colors;
    @Expose
    @SerializedName("versus")
    private ThumbnailForegroundVersusRead thumbnailForegroundVersus;
    @Expose
    @SerializedName("logo")
    private ThumbnailForegroundLogoRead thumbnailForegroundLogo;
    @Expose
    @SerializedName("customForeground")
    private boolean customForeground;
}
