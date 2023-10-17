package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Tournament implements Cloneable{

    @Expose
    @SerializedName("id")
    private String tournamentId;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("logo")
    private String image;
    @Expose
    @SerializedName("thumbnailSettings")
    private FileThumbnailSettings thumbnailSettings;
    @Expose
    @SerializedName("top8Settings")
    private FileTop8Settings top8Settings;

    public Tournament(String id, String name, String image,
                      FileThumbnailSettings thumbnailSettings, FileTop8Settings top8Settings){
        this.tournamentId = id;
        this.name = name;
        this.image = image;
        this.thumbnailSettings = thumbnailSettings;
        this.top8Settings = top8Settings;
    }

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    public boolean updateDifferences(Object obj){
        if (obj == null || !(obj instanceof Tournament)){
            return false;
        }
        boolean equal = true;
        Tournament tournament = (Tournament) obj;
        if (!this.tournamentId.equals(tournament.getTournamentId())){
            this.tournamentId = tournament.getTournamentId();
            equal = false;
        }
        if (!this.name.equals(tournament.getName())){
            this.name = tournament.getName();
            equal = false;
        }
        if (!this.image.equals(tournament.getImage())){
            this.image = tournament.getImage();
            equal = false;
        }
        if (!Objects.equals(this.thumbnailSettings,
                tournament.getThumbnailSettings())){
            this.thumbnailSettings = tournament.getThumbnailSettings();
            equal = false;
        }
        if (!Objects.equals(this.top8Settings,
                tournament.getTop8Settings())){
            this.top8Settings = tournament.getTop8Settings();
            equal = false;
        }
        return equal;
    }
}
