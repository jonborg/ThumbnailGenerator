package smashgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhaseGroupNodeGG {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("displayIdentifier")
    private String identifier;

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier == null ? "-" : identifier;
    }

    @Override
    public String toString() {
        return getIdentifier();
    }

    public boolean isNull(){
        return identifier == null;
    }
}
