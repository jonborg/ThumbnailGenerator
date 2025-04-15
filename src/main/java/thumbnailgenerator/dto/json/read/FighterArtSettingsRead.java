package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class FighterArtSettingsRead {
    @Expose
    @SerializedName("artType")
    private String artType;
    @Expose
    @SerializedName("fighterImageSettings")
    private String fighterImageSettingsPath;

}
