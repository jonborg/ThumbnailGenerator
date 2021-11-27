package thumbnail.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import exception.FighterImageSettingsNotFoundException;
import fighter.FighterImageSettings;
import java.util.List;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;

@Getter
public class ImageSettings {
    private final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ImageSettings.class);

    @Expose
    @SerializedName("mirrorPlayer2")
    private boolean mirrorPlayer2;
    @Expose
    @SerializedName("fighters")
    private List<FighterImageSettings> fighterImages;

    public void setMirrorPlayer2(boolean mirrorPlayer2) {
        this.mirrorPlayer2 = mirrorPlayer2;
    }

    public void setFighterImages(List<FighterImageSettings> fighterImages) {
        this.fighterImages = fighterImages;
    }

    public FighterImageSettings findFighterImageSettings(String fighterUrl)
            throws FighterImageSettingsNotFoundException{
        for (FighterImageSettings fighterImage : fighterImages){
            if (fighterUrl.equals(fighterImage.getFighter())){
                LOGGER.info("Loaded image settings for {}", fighterUrl);
                return fighterImage;
            }
        }
        LOGGER.error("Could not find image settings for {}", fighterUrl);
        throw new FighterImageSettingsNotFoundException(fighterUrl);
    }
}
