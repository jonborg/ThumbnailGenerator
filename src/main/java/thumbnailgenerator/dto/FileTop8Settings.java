package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.stream.Collectors;

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

    public FileTop8Settings(FileTop8Settings fileTop8Settings){
        this(
                fileTop8Settings.game,
                fileTop8Settings.getForeground(),
                fileTop8Settings.getBackground(),
                fileTop8Settings.getArtTypeDir()
                        .stream()
                        .map(f -> FighterArtSettings
                                .builder()
                                .artType(f.getArtType())
                                .fighterImageSettingsPath(f.getFighterImageSettingsPath())
                                .build()
                        )
                        .collect(Collectors.toList()),
                fileTop8Settings.getSlotSettingsFile()
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
