package thumbnail.image.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fighter.image.settings.FighterImageSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FighterImageThumbnailSettings extends FighterImageSettings {

    @Expose
    @SerializedName("fighter")
    private final String fighter;
}
