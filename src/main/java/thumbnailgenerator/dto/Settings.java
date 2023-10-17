package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import thumbnailgenerator.service.SmashUltimateFighterArtType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import lombok.var;

@Getter
@ToString
public class Settings {
    @Expose
    @SerializedName("foreground")
    protected String foreground;
    @Expose
    @SerializedName("background")
    protected String background;
    @Expose
    @SerializedName("artSettings")
    protected List<FighterArtSettings> artTypeDir;

    public Settings(String foreground, String background, List<FighterArtSettings> artTypeDir){
        this.foreground = foreground;
        this.background = background;
        this.artTypeDir = artTypeDir;
    }

    public String getFighterImageSettingsFile(
            SmashUltimateFighterArtType artType){
        var result = this.getArtTypeDir()
                .stream()
                .filter(dir -> artType.equals(dir.getArtType()))
                .findFirst();
        if (result.isPresent()){
            return result.get().getFighterImageSettingsPath();
        } else {
            return null;
        }
    }
}
