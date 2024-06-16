package converter;

import com.google.gson.reflect.TypeToken;
import exception.OnlineImageNotFoundException;
import fighter.DownloadFighterURL;
import fighter.Fighter;
import fighter.FighterArtType;
import fighter.image.FighterImage;
import fighter.image.ImageUtils;
import file.json.JSONReader;
import file.json.JSONWriter;
import lombok.var;
import thumbnail.image.settings.FighterImageThumbnailSettings;
import thumbnail.image.settings.ImageSettings;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ThumbnailFighterImageSettingsConverter {

    public static void convert(File file, FighterArtType artType)
            throws IOException {
        ImageSettings imageSettings = loadImageSettings(file);
        var commands = new StringBuffer()
                .append("invicta;20/02/20XX;" + artType + "\n");

        for(var imageSetting : imageSettings.getFighterImages()){
            var pair = FighterImage.convertFromAlternateRender(imageSetting.getFighter());
            Fighter fighter = new Fighter("", pair.getValue0(), pair.getValue1(), false );
            BufferedImage bufferedImage = DownloadFighterURL.getFighterImageOnline(fighter, artType);

            int newOffsetX = convertHorizontalOffset(imageSetting, bufferedImage);
            imageSetting.setOffset(new int[]{newOffsetX, imageSetting.getOffset()[1]});
            commands.append("t;t;"+fighter.getUrlName()+";"+fighter.getUrlName()+";"+fighter.getAlt()+";"+fighter.getAlt()+";t\n");
        }
        saveCommands(commands);
        saveImageSettings(imageSettings, file);
    }

    private static ImageSettings loadImageSettings(File file) {
        return (ImageSettings) JSONReader.getJSONArrayFromFile(file.getPath(),
                new TypeToken<ArrayList<ImageSettings>>() {}.getType())
                .get(0);
    }

    private static int convertHorizontalOffset(FighterImageThumbnailSettings imageSetting, BufferedImage bufferedImage) {
        float scale = imageSetting.getScale();
        int offsetX = imageSetting.getOffset()[0];
        BufferedImage scaledImage = ImageUtils.resizeImage(bufferedImage, scale);
        int width = scaledImage.getWidth();
        int widthDiff = width - 640;

        float result = widthDiff > 0 ?
                - ((float) widthDiff/2 - offsetX) :
                2 * offsetX;

        return Math.round(result);
    }

    private static void saveImageSettings(ImageSettings imageSettings, File file) {
        String folderPath = file.getParent();
        String newFilePath = folderPath + "/" + file.getName().replace(".json","Converted.json");

        JSONWriter.updateThumbnailImageSettingsFile(Collections.singletonList(imageSettings), newFilePath);
    }

    private static void saveCommands(StringBuffer commands) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("testAllImages.txt"));
        writer.write(commands.toString());

        writer.close();
    }


}
