package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class FileTop8SettingsRead {
    @Expose
    @SerializedName("game")
    protected String game;
    @Expose
    @SerializedName("foreground")
    protected String foreground;
    @Expose
    @SerializedName("background")
    protected String background;
    @Expose
    @SerializedName("artSettings")
    protected List<FighterArtSettingsRead> artTypeDir;
    @Expose
    @SerializedName("slotSettingsFile")
    private String slotSettingsFile;
}
