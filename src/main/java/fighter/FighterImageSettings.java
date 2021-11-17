package fighter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FighterImageSettings {

    @Expose
    @SerializedName("fighter")
    private final String fighter;

    @Expose
    @SerializedName("offset")
    private final  int[] offset = new int[] {0, 0};

    @Expose
    @SerializedName("scale")
    private final float scale = 1.0f;

    @Expose
    @SerializedName("flip")
    private final boolean flip = false;
}
