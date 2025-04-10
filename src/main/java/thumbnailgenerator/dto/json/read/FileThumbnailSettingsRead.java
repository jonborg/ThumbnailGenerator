package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class FileThumbnailSettingsRead {
    @Expose
    @SerializedName("game")
    private String game;
    @Expose
    @SerializedName("foreground")
    private String foreground;
    @Expose
    @SerializedName("background")
    private String background;
    @Expose
    @SerializedName("artSettings")
    private List<FighterArtSettingsRead> artTypeDir;

}
