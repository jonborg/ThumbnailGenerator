package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class SetGG {
    @Expose
    @SerializedName("nodes")
    private List<SetNodeGG> setNodes;
}
