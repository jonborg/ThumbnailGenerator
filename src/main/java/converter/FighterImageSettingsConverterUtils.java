package converter;

import com.google.gson.reflect.TypeToken;
import exception.OnlineImageNotFoundException;
import fighter.DownloadFighterURL;
import fighter.Fighter;
import fighter.FighterArtType;
import fighter.image.FighterImage;
import file.json.JSONReader;
import file.json.JSONWriter;
import lombok.var;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;

class FighterImageSettingsConverterUtils {

    static <T> T loadImageSettings(File file, Type type) throws IOException {
        var result = JSONReader
                .getJSONArrayFromFile(
                        file.getPath(),
                        type
                );
        if (result != null){
            return (T) result.get(0);
        }
        throw new IOException("Could not complete conversion");
    }

    static BufferedImage getFighterImage(String urlName, FighterArtType artType)
            throws OnlineImageNotFoundException {
        var pair = FighterImage.convertFromAlternateRender(urlName);
        Fighter fighter = new Fighter("", pair.getValue0(), pair.getValue1(), false );
        return DownloadFighterURL.getFighterImageOnline(fighter, artType);
    }

    static int convertHorizontalOffset(int scaledWidth, int offsetX) {
        int widthDiff = scaledWidth - 2*offsetX - 640;
        float result = widthDiff > 0 ?
                - ((float) widthDiff/2 ) :
                2 * offsetX;
        return Math.round(result);
    }

    static <T> void saveImageSettings(T imageSettings, File file) {
        String folderPath = file.getParent();
        String newFilePath = folderPath + "/" + file.getName().replace(".json","Converted.json");
        JSONWriter.updateImageSettingsFile(Collections.singletonList(imageSettings), newFilePath);
    }
}
