package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FullSlot {
    @Expose
    @SerializedName("width")
    private int width;

    @Expose
    @SerializedName("height")
    private int height;

    @Expose
    @SerializedName("slots")
    private List<PlayerSlot> slots;

}
