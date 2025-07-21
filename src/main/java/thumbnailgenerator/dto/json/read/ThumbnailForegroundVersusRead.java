package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThumbnailForegroundVersusRead {
    @Expose
    @SerializedName("image")
    protected String imagePath;
    @Expose
    @SerializedName("scale")
    protected float scale;
    @Expose
    @SerializedName("verticalOffset")
    protected int verticalOffset;
}
