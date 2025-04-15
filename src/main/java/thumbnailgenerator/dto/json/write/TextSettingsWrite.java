package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import thumbnailgenerator.dto.TextSettings;

public class TextSettingsWrite {
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("font")
    private String font;
    @Expose
    @SerializedName("bold")
    private boolean bold;
    @Expose
    @SerializedName("italic")
    private boolean italic;
    @Expose
    @SerializedName("shadow")
    private boolean shadow;
    @Expose
    @SerializedName("contour")
    private float contour;
    @Expose
    @SerializedName("sizeTop")
    private int sizeTop;
    @Expose
    @SerializedName("sizeBottom")
    private int sizeBottom;
    @Expose
    @SerializedName("angleTop")
    private float angleTop;
    @Expose
    @SerializedName("angleBottom")
    private float angleBottom;
    @Expose
    @SerializedName("downOffsetTop")
    private int[] downOffsetTop;
    @Expose
    @SerializedName("downOffsetBottom")
    private int[] downOffsetBottom;

    public TextSettingsWrite(TextSettings textSettings){
        this.id = textSettings.getTournamentId();
        this.font = textSettings.getFont();
        this.bold = textSettings.isBold();
        this.italic = textSettings.isItalic();
        this.shadow = textSettings.isShadow();
        this.contour = textSettings.getContour();
        this.sizeTop = textSettings.getSizeTop();
        this.sizeBottom = textSettings.getSizeBottom();
        this.angleTop = textSettings.getAngleTop();
        this.angleBottom = textSettings.getAngleBottom();
        this.downOffsetTop = textSettings.getDownOffsetTop();
        this.downOffsetBottom = textSettings.getDownOffsetBottom();
    }
}
