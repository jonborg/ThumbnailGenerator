package smashgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhaseGroupGG {
    @Expose
    @SerializedName("nodes")
    private List<PhaseGroupNodeGG> nodes;

    public List<PhaseGroupNodeGG> getNodes() {
        return nodes;
    }
}
