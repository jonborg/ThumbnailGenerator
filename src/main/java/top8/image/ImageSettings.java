package top8.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import exception.FighterImageSettingsNotFoundException;
import java.util.List;
import lombok.Getter;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public class ImageSettings {
    private final Logger LOGGER = LogManager.getLogger(ImageSettings.class);

    @Expose
    @SerializedName("fighters")
    private List<FighterImageSettings> fighterImages;

    public void setFighterImages(List<FighterImageSettings> fighterImages) {
        this.fighterImages = fighterImages;
    }

    public FighterImageSettings findFighterImageSettings(String fighterUrl)
            throws FighterImageSettingsNotFoundException{
        var fighterImage = fighterImages.stream()
                .filter(f -> fighterUrl.equals(f.getFighter()))
                .findFirst()
                .orElse(null);
        if (fighterImage == null){
            LOGGER.error("Could not find image settings for {}", fighterUrl);
            throw new FighterImageSettingsNotFoundException(fighterUrl);
        }
        LOGGER.info("Loaded image settings for {}", fighterUrl);
        LOGGER.debug(fighterImage.toString());
        return fighterImage;
    }
}
