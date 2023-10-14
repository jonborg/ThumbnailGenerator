package top8.image.slot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class AdditionalFighters {
    @Expose
    @SerializedName("scale")
    private float scale;

    @Expose
    @SerializedName("coordinate_x")
    private String coordinateX;

    @Expose
    @SerializedName("coordinate_y")
    private String coordinateY;
}
