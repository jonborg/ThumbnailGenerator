package smashgg.tournament;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.List;

public class PhaseGroupGG {
    @Expose
    @SerializedName("nodes")
    private List<PhaseGroupNodeGG> nodes;

    public List<PhaseGroupNodeGG> getNodes() {
        return nodes;
    }
}
