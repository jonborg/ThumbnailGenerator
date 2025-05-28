package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FighterImageThumbnailSettings extends FighterImageSettings {

    @Expose
    @SerializedName("fighter")
    private final String fighter;

    public FighterImageThumbnailSettings(String fighter, int[] offset, float scale, boolean flip) {
        super();
        this.fighter = fighter;
        this.offset = offset;
        this.scale = scale;
        this.flip = flip;
    }
}
