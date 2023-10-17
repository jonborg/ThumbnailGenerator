package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileTop8Settings extends Settings {
    @Expose
    @SerializedName("slotSettingsFile")
    private String slotSettingsFile;

    public FileTop8Settings(String foreground, String background,
                            List<FighterArtSettings> artTypeDir, String slotSettingsFile){
        super(foreground, background, artTypeDir);
        this.slotSettingsFile = slotSettingsFile;
    }
}
