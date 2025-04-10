package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import thumbnailgenerator.dto.FighterArtSettings;

public class FighterArtSettingsWrite {
    @Expose
    @SerializedName("artType")
    private String artType;
    @Expose
    @SerializedName("fighterImageSettings")
    private String fighterImageSettingsPath;
    public FighterArtSettingsWrite(FighterArtSettings fighterArtSettings){
        this.artType = fighterArtSettings.getArtType().getEnumName();
        this.fighterImageSettingsPath = fighterArtSettings.getFighterImageSettingsPath();
    }
}
