package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import thumbnailgenerator.dto.FighterArtSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.dto.Game;

import java.util.List;
import java.util.stream.Collectors;

public class FileTop8SettingsWrite {
    @Expose
    @SerializedName("game")
    protected String game;
    @Expose
    @SerializedName("foreground")
    protected String foreground;
    @Expose
    @SerializedName("background")
    protected String background;
    @Expose
    @SerializedName("slotSettingsFile")
    private String slotSettingsFile;
    @Expose
    @SerializedName("artSettings")
    protected List<FighterArtSettingsWrite> artTypeDir;

    public FileTop8SettingsWrite(FileTop8Settings fileTop8Settings){
        this.game = fileTop8Settings.getGame().name();
        this.foreground = fileTop8Settings.getForeground();
        this.background = fileTop8Settings.getBackground();
        this.artTypeDir = fileTop8Settings.getArtTypeDir()
                .stream()
                .map(atd -> new FighterArtSettingsWrite(atd))
                .collect(Collectors.toList());
        this.slotSettingsFile = fileTop8Settings.getSlotSettingsFile();
    }
}
