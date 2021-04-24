package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SetGG {
    @Expose
    @SerializedName("nodes")
    private List<SetNodeGG> setNodes;

    public List<SetNodeGG> getSetNodes() {
        return setNodes;
    }
}
