package thumbnailgenerator.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.GraphicGenerated;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.utils.file.FileUtils;

@Service
public abstract class CharacterImageFetcher {

    private static final Logger LOGGER = LogManager.getLogger(
            CharacterImageFetcher.class);

    abstract URL getOnlineUrl(Fighter fighter, GraphicGenerated graphicGenerated)
            throws MalformedURLException;

    public BufferedImage getCharacterImage(Fighter fighter, GraphicGenerated graphicGenerated)
            throws OnlineImageNotFoundException, MalformedURLException {
        if (graphicGenerated.isLocally()) {
            try {
                return getFighterImageLocally(fighter, graphicGenerated);
            }  catch (IOException e) {
                LOGGER.debug("Image for {} does not exist locally. Will now try finding it online.", fighter.getUrlName());
                var url = getOnlineUrl(fighter, graphicGenerated);
                var image = getFighterImageOnline(fighter, url);
                saveImageLocally(fighter, graphicGenerated, image);
                return image;
            }
        } else {
            var url = getOnlineUrl(fighter, graphicGenerated);
            return getFighterImageOnline(fighter, url);
        }
    }

    public void saveImage(BufferedImage image, File file) {
        try {
            LOGGER.info("Saving thumbnail {} on {}", file.getName(), file.getAbsolutePath());
            ImageIO.write(image, "png", file);
            LOGGER.info("Thumbnail saved successfully.");
        } catch (IOException e) {
            LOGGER.error("Thumbnail could not be saved.");
            LOGGER.catching(e);
        }
    }

    private BufferedImage getFighterImageLocally(Fighter fighter, GraphicGenerated graphicGenerated) throws IOException {
        var localCharacterImageRootPath = FileUtils.getLocalFightersPath(graphicGenerated);
        var localCharacterImagePath = localCharacterImageRootPath + fighter.getUrlName() + "/";

        var rootDirectory = new File(localCharacterImageRootPath);
        var fighterDirectory = new File(localCharacterImagePath);

        if (!rootDirectory.exists()) rootDirectory.mkdir();
        if (!fighterDirectory.exists()) fighterDirectory.mkdir();

        File localImage = new File(localCharacterImagePath + fighter.getAlt()+".png");

        LOGGER.debug("Trying to find local image for alt {} of {}.", fighter.getAlt(), fighter.getName());
        return ImageIO.read(localImage);
    }

    private void saveImageLocally(Fighter fighter, GraphicGenerated graphicGenerated, BufferedImage image){
        var localCharacterImagePath = FileUtils.getLocalFightersPath(graphicGenerated);
        var fighterDirPath = localCharacterImagePath + fighter.getUrlName() + "/";

        File localImage = new File(fighterDirPath + fighter.getAlt()+".png");
        saveImage(image, localImage);
    }

    private BufferedImage getFighterImageOnline(Fighter fighter, URL url) throws
            OnlineImageNotFoundException {
        try {
            LOGGER.debug("Trying to find image online for alt {} of {}: {}",
                    fighter.getAlt(), fighter.getName(),url.getHost() + url.getPath());
            return ImageIO.read(url);
        }catch(IOException e){
            LOGGER.error("An issue occurred when finding image for alt {} of {}. URI: {}",
                    fighter.getAlt(), fighter.getName(), url);
            LOGGER.catching(e);
            throw new OnlineImageNotFoundException();
        }
    }
}
