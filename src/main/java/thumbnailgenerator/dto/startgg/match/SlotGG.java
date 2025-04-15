package thumbnailgenerator.dto.startgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SlotGG {
    @Expose
    @SerializedName("entrant")
    private EntrantGG entrant;

}
