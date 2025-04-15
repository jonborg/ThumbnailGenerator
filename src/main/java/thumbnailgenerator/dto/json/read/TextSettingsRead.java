package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.utils.json.JSONReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TextSettingsRead {
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

    private static String textSettingsFile = "settings/thumbnails/text/text.json";
}
