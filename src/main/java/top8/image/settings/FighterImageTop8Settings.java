package top8.image.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FighterImageTop8Settings {

    @Expose
    @SerializedName("fighter")
    private final String fighter;

    @Expose
    @SerializedName("slots")
    private List<SlotImageTop8Settings> slotImageTop8Settings;

    public SlotImageTop8Settings getSlotImageTop8Settings(int slotIndex){
        return slotImageTop8Settings.stream()
                .filter(s -> Objects.equals(slotIndex, s.getSlot()))
                .findFirst()
                .orElse(null);
    }
}
