package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FighterImageThumbnailSettings extends FighterImageSettings {

    @Expose
    @SerializedName("fighter")
    private final String fighter;
}
