package top8.generate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class CharacterSlot {
    @Expose
    @SerializedName("place")
    private int place;

    @Expose
    @SerializedName("mask")
    private String mask;

    @Expose
    @SerializedName("coordinate_y")
    private int coordinateX;

    @Expose
    @SerializedName("coordinate_x")
    private int coordinateY;
}

