package top8.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SlotImageSettings {

    @Expose
    @SerializedName("slot")
    private final int slot;

    @Expose
    @SerializedName("offset")
    private final int[] offset = new int[] {0, 0};

    @Expose
    @SerializedName("scale")
    private final float scale;

    @Expose
    @SerializedName("flip")
    private final boolean flip;
}
