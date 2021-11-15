package fighter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FighterImageSettings {

    @Expose
    @SerializedName("fighter")
    private String fighter;

    @Expose
    @SerializedName("offset")
    private int[] offset = new int[] {0, 0};

    @Expose
    @SerializedName("scale")
    private float scale = 1.0f;

    @Expose
    @SerializedName("flip")
    private boolean flip = false;

    public String getFighter() {
        return fighter;
    }

    public void setFighter(String fighter) {
        this.fighter = fighter;
    }

    public void setOffset(int[] offset) {
        this.offset = offset;
    }

    public int[] getOffset() {
        return offset;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }
}
