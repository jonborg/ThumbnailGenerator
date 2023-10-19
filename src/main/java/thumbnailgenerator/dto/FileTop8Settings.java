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
public class FileTop8Settings extends Settings implements Cloneable {
    @Expose
    @SerializedName("slotSettingsFile")
    private String slotSettingsFile;

    public FileTop8Settings(Game game, String foreground, String background,
                            List<FighterArtSettings> artTypeDir, String slotSettingsFile){
        super(game, foreground, background, artTypeDir);
        this.slotSettingsFile = slotSettingsFile;
    }

    @Override
    public FileTop8Settings clone() {
        FileTop8Settings clone = null;
        try {
            clone = (FileTop8Settings) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
