package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GameGG {
    @Expose
    @SerializedName("selections")
    private List<SelectionGG> selections;

    public List<SelectionGG> getSelections() {
        return selections;
    }


}
