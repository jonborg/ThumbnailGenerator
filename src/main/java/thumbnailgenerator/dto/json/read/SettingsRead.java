package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;
import thumbnailgenerator.dto.Game;

import java.util.List;

@Getter
@Setter
@ToString
public class SettingsRead {
    @Expose
    @SerializedName("game")
    protected String game;
    @Expose
    @SerializedName("background")
    protected String background;
    @Expose
    @SerializedName("artSettings")
    protected List<FighterArtSettingsRead> artTypeDir;
}
