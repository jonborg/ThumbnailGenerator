package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectionGG {
    @Expose
    @SerializedName("entrant")
    private EntrantGG entrant;
    @Expose
    @SerializedName("selectionValue")
    private int selectionValue;
    @Expose
    @SerializedName("selectionType")
    private String selectionType;

    public EntrantGG getEntrant() {
        return entrant;
    }

    public int getSelectionValue() {
        return selectionValue;
    }

    public String getSelectionType() {
        return selectionType;
    }
}
