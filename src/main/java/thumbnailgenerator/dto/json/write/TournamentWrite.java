package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import thumbnailgenerator.dto.Tournament;

import java.util.List;
import java.util.stream.Collectors;

public class TournamentWrite {
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
    private List<FileThumbnailSettingsWrite> thumbnailSettings;
    @Expose
    @SerializedName("top8Settings")
    private List<FileTop8SettingsWrite> top8Settings;

    public TournamentWrite(Tournament tournament){
        this.id = tournament.getTournamentId();
        this.name = tournament.getName();
        this.image = tournament.getImage();
        this.thumbnailSettings = tournament.getThumbnailSettings()
                .stream()
                .map(ts -> new FileThumbnailSettingsWrite(ts))
                .collect(Collectors.toList());
        this.top8Settings = tournament.getTop8Settings()
                .stream()
                .map(ts -> new FileTop8SettingsWrite(ts))
                .collect(Collectors.toList());
    }
}
