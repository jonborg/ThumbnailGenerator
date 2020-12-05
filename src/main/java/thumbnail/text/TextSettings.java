package thumbnail.text;

import json.JSONProcessor;
import org.json.simple.JSONObject;

public class TextSettings {
    private String font;
    private boolean bold;
    private boolean italic;

    private boolean shadow;
    private float contour;

    private int sizeTop;
    private int sizeBottom;

    private float angleTop;
    private float angleBottom;

    private int[] downOffsetTop;
    private int[] downOffsetBottom;


    public TextSettings(JSONObject settings){
        this.font = settings.containsKey("font") ? (String) settings.get("font") : "BebasNeue-Regular";
        this.bold = JSONProcessor.toBoolean(settings,"bold");
        this.italic = JSONProcessor.toBoolean(settings,"italic");

        this.shadow = JSONProcessor.toBoolean(settings,"shadow");
        this.contour = JSONProcessor.toFloat(settings,"contour");

        this.sizeTop = JSONProcessor.toInt(settings,"sizeTop");
        this.sizeBottom = JSONProcessor.toInt(settings,"sizeBottom");

        this.angleTop = JSONProcessor.toFloat(settings,"angleTop");
        this.angleBottom = JSONProcessor.toFloat(settings,"angleBottom");

        this.downOffsetTop = JSONProcessor.toIntArray(settings,"downOffsetTop");
        this.downOffsetBottom = JSONProcessor.toIntArray(settings,"downOffsetBottom");
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
