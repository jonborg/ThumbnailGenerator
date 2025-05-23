package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SlotImageTop8Settings extends FighterImageSettings {

    @Expose
    @SerializedName("slot")
    private final int slot;

}
