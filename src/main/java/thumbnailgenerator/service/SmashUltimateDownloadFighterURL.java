package thumbnailgenerator.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.utils.file.FileUtils;

public class SmashUltimateDownloadFighterURL implements DownloadFighterURL<SmashUltimateFighterArtType> {
    private static final Logger LOGGER = LogManager.getLogger(
            SmashUltimateDownloadFighterURL.class);

    static String FIGHTERS_URL = "https://www.smashbros.com/assets_v2/img/fighter/";
    static String FIGHTERS_URL_2 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/renders/";
    static String SANS_URL = "https://i.redd.it/n2tcplon8qk31.png";
    static String MURAL_URL = "https://raw.githubusercontent.com/jonborg/ThumbnailGenerator/dev/assets/mural/";

    public String generateFighterURL(String urlName, int alt, SmashUltimateFighterArtType artType) {
        var path = FileUtils.getLocalFightersPath(artType) ;
        var fighterPath = System.getProperty("user.dir") + path + urlName + "/" + alt +".png";
        var file = new File(fighterPath);
        if (!file.exists()){
            LOGGER.debug("Image for {}{} does not exist locally. Will try finding online", urlName, alt);
            return getOnlineURL(urlName,alt, artType);
        }
        return fighterPath;
    }

    public BufferedImage getFighterImageOnline(Fighter fighter, SmashUltimateFighterArtType artType) throws
            OnlineImageNotFoundException {
        try {
            URL url = new URL(getOnlineURL(fighter.getUrlName(), fighter.getAlt(), artType));
            LOGGER.debug("Trying to find image online for alt {} of {}: {}", fighter.getAlt(), fighter.getName(),url.getHost() + url.getPath());
            return ImageIO.read(url);
        }catch(IOException e){
            LOGGER.error("An issue occurred when finding image for alt {} of {}. URI: {}",
                    fighter.getAlt(), fighter.getName(), getOnlineURL(fighter.getUrlName(), fighter.getAlt(), artType));
            LOGGER.catching(e);
            throw new OnlineImageNotFoundException();
        }
    }

    private String getOnlineURL(String urlName, int alt, SmashUltimateFighterArtType artType) {
        String urlString;
        switch (artType) {
            case MURAL:
                urlString = MURAL_URL + urlName + "/" + alt + ".png";
                break;
            case RENDER:
            default:
                if (alt == 1) {
                    urlString = FIGHTERS_URL + urlName + "/main.png";
                } else {
                    urlString = FIGHTERS_URL + urlName + "/main" + alt + ".png";
                }

                if (urlName.contains("pokemon_trainer")) {
                    urlString =
                            FIGHTERS_URL_2 + "misc/pokemon-trainer-0" + alt +
                                    ".png";
                }
                if (urlName.contains("mii_brawler")) {
                    urlString = FIGHTERS_URL_2 + "fighters/51/01.png";
                }
                if (urlName.contains("mii_swordfighter")) {
                    urlString = FIGHTERS_URL_2 + "fighters/52/01.png";
                }
                if (urlName.contains("mii_gunner")) {
                    if (alt == 1)
                        urlString = FIGHTERS_URL_2 + "fighters/53/01.png";
                    if (alt == 2) urlString = SANS_URL;
                }
                break;
        }
        return urlString;
    }
}