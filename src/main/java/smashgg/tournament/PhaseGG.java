package smashgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class PhaseGG {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("phaseGroups")
    private PhaseGroupGG phaseGroup;

    public List<PhaseGroupNodeGG> getPhaseGroups() {
        return phaseGroup.getNodes();
    }

    @Override
    public String toString() {
        return name == null ? "-" : name;
    }

    public boolean isNull(){
        return name == null;
    }
}
