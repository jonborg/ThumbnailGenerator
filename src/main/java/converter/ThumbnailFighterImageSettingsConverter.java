package converter;

import com.google.gson.reflect.TypeToken;
import exception.OnlineImageNotFoundException;
import fighter.FighterArtType;
import fighter.image.ImageUtils;
import lombok.var;
import thumbnail.image.settings.FighterImageThumbnailSettings;
import thumbnail.image.settings.ImageSettings;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ThumbnailFighterImageSettingsConverter {

    public static void convertThumbnailImageSettings(File file, FighterArtType artType)
            throws IOException {
        ImageSettings imageSettings = FighterImageSettingsConverterUtils
                .loadImageSettings(
                        file,
                        new TypeToken<ArrayList<ImageSettings>>() {}.getType()
                );
        for(var imageSetting : imageSettings.getFighterImages()){
            try {
                BufferedImage bufferedImage = FighterImageSettingsConverterUtils
                        .getFighterImage(
                                imageSetting.getFighter(),
                                artType
                        );
                int newOffsetX =
                        convertHorizontalOffset(imageSetting, bufferedImage);
                imageSetting.setOffset(
                        new int[] {newOffsetX, imageSetting.getOffset()[1]});
            } catch (OnlineImageNotFoundException ignored) {
            }
        }
        FighterImageSettingsConverterUtils.saveImageSettings(imageSettings, file);
    }

    static int convertHorizontalOffset(FighterImageThumbnailSettings imageSetting, BufferedImage bufferedImage) {
        float scale = imageSetting.getScale();
        int offsetX = imageSetting.getOffset()[0];
        BufferedImage scaledImage = ImageUtils.resizeImage(bufferedImage, scale);
        int scaledWidth = scaledImage.getWidth();
        return FighterImageSettingsConverterUtils.convertHorizontalOffset(scaledWidth, offsetX);
    }
}
