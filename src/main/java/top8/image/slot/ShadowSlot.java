package top8.image.slot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShadowSlot {

    @Expose
    @SerializedName("rgbColor")
    private String rgbColor;

    @Expose
    @SerializedName("coordinate_x")
    private int coordinateX;

    @Expose
    @SerializedName("coordinate_y")
    private int coordinateY;

    public int getColor(){
        if(rgbColor == null || rgbColor.length()!=6){
            return 0;
        }
        var r = Integer.parseInt(rgbColor.substring(0,2),16);
        var g = Integer.parseInt(rgbColor.substring(2,4),16);
        var b = Integer.parseInt(rgbColor.substring(4,6),16);
        return ((00) << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

    public int getScaledX(float scale){
        return (int) Math.floor(coordinateX * scale);
    }

    public int getScaledY(float scale){
        return (int) Math.floor(coordinateY * scale);
    }

}
