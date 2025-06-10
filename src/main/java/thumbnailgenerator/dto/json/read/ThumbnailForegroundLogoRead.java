package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThumbnailForegroundLogoRead {
    @Expose
    @SerializedName("logo")
    protected String logo;
    @Expose
    @SerializedName("scale")
    protected float scale;
    @Expose
    @SerializedName("verticalOffset")
    protected int verticalOffset;
    @Expose
    @SerializedName("aboveForeground")
    protected boolean aboveForeground;
}
