package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlotGG {
    @Expose
    @SerializedName("entrant")
    private EntrantGG entrant;

    public EntrantGG getEntrant() {
        return entrant;
    }


}
