package thumbnail.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import exception.FighterImageSettingsNotFoundException;
import fighter.FighterImageSettings;

import java.util.List;

public class ImageSettings {
    @Expose
    @SerializedName("mirrorPlayer2")
    private boolean mirrorPlayer2;
    @Expose
    @SerializedName("fighters")
    private List<FighterImageSettings> fighterImages;

    public boolean isMirrorPlayer2() {
        return mirrorPlayer2;
    }

    public void setMirrorPlayer2(boolean mirrorPlayer2) {
        this.mirrorPlayer2 = mirrorPlayer2;
    }

    public List<FighterImageSettings> getFighterImages() {
        return fighterImages;
    }

    public void setFighterImages(List<FighterImageSettings> fighterImages) {
        this.fighterImages = fighterImages;
    }

    public FighterImageSettings findFighterImageSettings(String fighterUrl)
            throws FighterImageSettingsNotFoundException{
        for (FighterImageSettings fighterImage : fighterImages){
            if (fighterUrl.equals(fighterImage.getFighter())){
                return fighterImage;
            }
        }
        throw new FighterImageSettingsNotFoundException(fighterUrl);
    }
}
