package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.ThumbnailForegroundVersus;

@Getter
@Setter
public class ThumbnailForegroundVersusWrite {
    @Expose
    @SerializedName("image")
    private String imagePath;
    @Expose
    @SerializedName("scale")
    private float scale;
    @Expose
    @SerializedName("verticalOffset")
    private int verticalOffset;

    public ThumbnailForegroundVersusWrite(ThumbnailForegroundVersus thumbnailForegroundVersus){
        this.imagePath = thumbnailForegroundVersus.getImagePath();
        this.scale = thumbnailForegroundVersus.getScale();
        this.verticalOffset = thumbnailForegroundVersus.getVerticalOffset();
    }
}
