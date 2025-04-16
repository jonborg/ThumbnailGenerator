package thumbnailgenerator.dto.startgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class PhaseGroupNodeGG {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("displayIdentifier")
    private String identifier;

    @Override
    public String toString() {
        return identifier == null ? "-" : identifier;
    }

    public boolean isNull(){
        return identifier == null;
    }
}
