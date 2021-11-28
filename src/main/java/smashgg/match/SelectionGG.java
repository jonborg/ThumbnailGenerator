package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
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
}
