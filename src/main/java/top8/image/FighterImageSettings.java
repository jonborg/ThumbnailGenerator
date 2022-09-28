package top8.image;

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
public class FighterImageSettings {

    @Expose
    @SerializedName("fighter")
    private final String fighter;

    @Expose
    @SerializedName("slots")
    private List<SlotImageSettings> slotImageSettings;

    public SlotImageSettings getSlotImageSettings(int slotIndex){
        return slotImageSettings.stream()
                .filter(s -> Objects.equals(slotIndex, s.getSlot()))
                .findFirst()
                .orElse(null);
    }
}
