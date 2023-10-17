package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;

@Getter
public class Top8ImageSettings {
    private final Logger LOGGER = LogManager.getLogger(Top8ImageSettings.class);

    @Expose
    @SerializedName("fighters")
    private List<FighterImageTop8Settings> fighterImages;

    public void setFighterImages(List<FighterImageTop8Settings> fighterImages) {
        this.fighterImages = fighterImages;
    }

    public FighterImageTop8Settings findFighterImageSettings(String fighterUrl)
            throws FighterImageSettingsNotFoundException {
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
