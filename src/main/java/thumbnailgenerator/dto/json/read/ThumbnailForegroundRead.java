package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.ThumbnailForeground;
import thumbnailgenerator.dto.json.write.ThumbnailForegroundLogoWrite;

import java.util.Map;

@Getter
@Setter
public class ThumbnailForegroundRead {
    @Expose
    @SerializedName("foreground")
    private String foreground;
    @Expose
    @SerializedName("colors")
    private Map<String, String> colors;
    @Expose
    @SerializedName("foregroundLogo")
    private ThumbnailForegroundLogoRead thumbnailForegroundLogo;
    @Expose
    @SerializedName("customForeground")
    private boolean customForeground;
}
