package top8.image.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import exception.FighterImageSettingsNotFoundException;
import java.util.List;
import lombok.Getter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public class ImageSettings {
    private final Logger LOGGER = LogManager.getLogger(ImageSettings.class);

    @Expose
    @SerializedName("fighters")
    private List<FighterImageTop8Settings> fighterImages;

    public void setFighterImages(List<FighterImageTop8Settings> fighterImages) {
        this.fighterImages = fighterImages;
    }

    public FighterImageTop8Settings findFighterImageSettings(String fighterUrl)
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
