package startgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class PhaseGroupGG {
    @Expose
    @SerializedName("nodes")
    private List<PhaseGroupNodeGG> nodes;
}
