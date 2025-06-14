package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.ThumbnailForegroundLogo;

@Getter
@Setter
public class ThumbnailForegroundLogoWrite {
    @Expose
    @SerializedName("image")
    private String imagePath;
    @Expose
    @SerializedName("scale")
    private float scale;
    @Expose
    @SerializedName("verticalOffset")
    private int verticalOffset;
    @Expose
    @SerializedName("aboveForeground")
    private boolean aboveForeground;

    public ThumbnailForegroundLogoWrite(ThumbnailForegroundLogo thumbnailForegroundLogo){
        this.imagePath = thumbnailForegroundLogo.getImagePath();
        this.scale = thumbnailForegroundLogo.getScale();
        this.verticalOffset = thumbnailForegroundLogo.getVerticalOffset();
        this.aboveForeground = thumbnailForegroundLogo.isAboveForeground();
    }
}
