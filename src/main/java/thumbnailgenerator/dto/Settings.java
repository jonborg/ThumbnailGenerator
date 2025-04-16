package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Setter;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
@Setter
@ToString
public class Settings {
    @Expose
    @SerializedName("game")
    protected Game game;
    @Expose
    @SerializedName("foreground")
    protected String foreground;
    @Expose
    @SerializedName("background")
    protected String background;
    @Expose
    @SerializedName("artSettings")
    protected List<FighterArtSettings> artTypeDir;

    public Settings(Game game, String foreground, String background, List<FighterArtSettings> artTypeDir){
        this.game = game;
        this.foreground = foreground;
        this.background = background;
        this.artTypeDir = artTypeDir;
    }

    public String getFighterImageSettingsFile(
            FighterArtType artType){
        return this
                .getArtTypeDir()
                .stream()
                .filter(dir -> artType.equals(dir.getArtType()))
                .findFirst()
                .map(FighterArtSettings::getFighterImageSettingsPath)
                .get();
    }
}
