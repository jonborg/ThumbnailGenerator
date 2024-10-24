package converter;

import com.google.gson.reflect.TypeToken;
import fighter.FighterArtType;
import fighter.image.ImageUtils;
import lombok.var;
import top8.image.settings.ImageSettings;
import top8.image.settings.SlotImageTop8Settings;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Top8FighterImageSettingsConverter {

    public static void convertTop8ImageSettings(File file, FighterArtType artType)
            throws IOException {
        ImageSettings imageSettings = FighterImageSettingsConverterUtils
                .loadImageSettings(
                        file,
                        new TypeToken<ArrayList<ImageSettings>>() {}.getType()
                );

        for(var imageSetting : imageSettings.getFighterImages()){
            BufferedImage bufferedImage = FighterImageSettingsConverterUtils
                    .getFighterImage(
                            imageSetting.getFighter(),
                            artType
                    );
            for (var slotSetting : imageSetting.getSlotImageTop8Settings()) {
                try {
                    int newOffsetX =
                            convertHorizontalOffset(slotSetting, bufferedImage);
                    slotSetting.setOffset(
                            new int[] {newOffsetX, slotSetting.getOffset()[1]});
                } catch (Exception ignored){
                }
            }
        }
        FighterImageSettingsConverterUtils.saveImageSettings(imageSettings, file);
    }

    static int convertHorizontalOffset(SlotImageTop8Settings imageSetting, BufferedImage bufferedImage) {
        float scale = imageSetting.getScale();
        int offsetX = imageSetting.getOffset()[0];
        BufferedImage scaledImage = ImageUtils.resizeImage(bufferedImage, scale);
        int width = scaledImage.getWidth();
        return FighterImageSettingsConverterUtils.convertHorizontalOffset(width, offsetX);
    }
}
