package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class TournamentRead {
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("logo")
    private String image;
    @Expose
    @SerializedName("thumbnailSettings")
    private List<FileThumbnailSettingsRead> thumbnailSettings;
    @Expose
    @SerializedName("top8Settings")
    private List<FileTop8SettingsRead> top8Settings;
}
