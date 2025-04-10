package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import thumbnailgenerator.dto.json.read.FighterArtSettingsRead;
import thumbnailgenerator.dto.json.read.FileTop8SettingsRead;
import thumbnailgenerator.enums.interfaces.FighterArtType;
import thumbnailgenerator.utils.enums.FighterArtTypeUtils;

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

    public FileTop8Settings(FileTop8SettingsRead fileTop8SettingsRead){
        this(
                Game.valueOf(fileTop8SettingsRead.getGame()),
                fileTop8SettingsRead.getForeground(),
                fileTop8SettingsRead.getBackground(),
                FighterArtTypeUtils.convertArtSettings(fileTop8SettingsRead.getArtTypeDir(), Game.valueOf(fileTop8SettingsRead.getGame())),
                fileTop8SettingsRead.getSlotSettingsFile()
        );
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
