package thumbnail.text;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import json.JSONReader;

import java.util.ArrayList;
import java.util.List;

public class TextSettings {
    @SerializedName("id")
    private String tournamentId;
    @SerializedName("font")
    private String font;
    @SerializedName("bold")
    private boolean bold;
    @SerializedName("italic")
    private boolean italic;
    @SerializedName("shadow")
    private boolean shadow;
    @SerializedName("contour")
    private float contour;
    @SerializedName("sizeTop")
    private int sizeTop;
    @SerializedName("sizeBottom")
    private int sizeBottom;
    @SerializedName("angleTop")
    private float angleTop;
    @SerializedName("angleBottom")
    private float angleBottom;
    @SerializedName("downOffsetTop")
    private int[] downOffsetTop;
    @SerializedName("downOffsetBottom")
    private int[] downOffsetBottom;

    private static String textFile = "settings/thumbnails/text/text.json";

    public TextSettings(String tournamentId, String font, boolean bold, boolean italic, boolean shadow, float contour, int sizeTop, int sizeBottom, float angleTop, float angleBottom, int[] downOffsetTop, int[] downOffsetBottom) {
        this.tournamentId = tournamentId;
        this.font = font;
        this.bold = bold;
        this.italic = italic;
        this.shadow = shadow;
        this.contour = contour;
        this.sizeTop = sizeTop;
        this.sizeBottom = sizeBottom;
        this.angleTop = angleTop;
        this.angleBottom = angleBottom;
        this.downOffsetTop = downOffsetTop;
        this.downOffsetBottom = downOffsetBottom;
    }

    public static TextSettings loadTextSettings(String tournamentId) {

        List<TextSettings> textSettingsList =
                JSONReader.getJSONArray(textFile, new TypeToken<ArrayList<TextSettings>>() {
                }.getType());
        for (TextSettings textSettings : textSettingsList) {
            if (textSettings.getTournamentId().equals(tournamentId)) {
                return textSettings;
            }
        }
        return null;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public boolean hasBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean hasItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean hasShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public float getContour() {
        return contour;
    }

    public void setContour(float contour) {
        this.contour = contour;
    }

    public int getSizeTop() {
        return sizeTop;
    }

    public void setSizeTop(int sizeTop) {
        this.sizeTop = sizeTop;
    }

    public int getSizeBottom() {
        return sizeBottom;
    }

    public void setSizeBottom(int sizeBottom) {
        this.sizeBottom = sizeBottom;
    }

    public float getAngleTop() {
        return angleTop;
    }

    public void setAngleTop(float angleTop) {
        this.angleTop = angleTop;
    }

    public float getAngleBottom() {
        return angleBottom;
    }

    public void setAngleBottom(float angleBottom) {
        this.angleBottom = angleBottom;
    }

    public int[] getDownOffsetTop() {
        return downOffsetTop;
    }

    public void setDownOffsetTop(int[] downOffsetTop) {
        this.downOffsetTop = downOffsetTop;
    }

    public int[] getDownOffsetBottom() {
        return downOffsetBottom;
    }

    public void setDownOffsetBottom(int[] downOffsetBottom) {
        this.downOffsetBottom = downOffsetBottom;
    }
}
