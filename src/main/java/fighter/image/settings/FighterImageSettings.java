package fighter.image.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public abstract class FighterImageSettings {

    @Expose
    @SerializedName("offset")
    protected int[] offset = new int[] {0, 0};

    @Expose
    @SerializedName("scale")
    protected float scale;

    @Expose
    @SerializedName("flip")
    protected boolean flip = false;

}
