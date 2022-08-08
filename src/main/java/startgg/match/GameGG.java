package startgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class GameGG {
    @Expose
    @SerializedName("selections")
    private List<SelectionGG> selections;
}
