package fighter;

import file.FileUtils;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
public class DownloadFighterURL {
    private static final Logger LOGGER = LogManager.getLogger(DownloadFighterURL.class);

    static String FIGHTERS_URL = "https://www.smashbros.com/assets_v2/img/fighter/";
    static String FIGHTERS_URL_2 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/renders/";
    static String SANS_URL = "https://i.redd.it/n2tcplon8qk31.png";
    static String MURAL_URL = "https://raw.githubusercontent.com/jonborg/ThumbnailGenerator/dev/assets/mural/";
    static String RENDER_URL = "https://raw.githubusercontent.com/jonborg/ThumbnailGenerator/dev/assets/render/";

    public static String generateFighterURL(String urlName, int alt, FighterArtType artType) {
        var path = FileUtils.getLocalFightersPath(artType) ;
        var fighterPath = System.getProperty("user.dir") + path + urlName + "/" + alt +".png";
        var file = new File(fighterPath);
        if (!file.exists()){
            LOGGER.debug("Image for {}{} does not exist locally. Will try finding online", urlName, alt);
            return getOnlineURL(urlName,alt, artType);
        }
        return fighterPath;
    }

    public static String getOnlineURL(String urlName, int alt, FighterArtType artType) {
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
                if (urlName.contains("random")) {
                    if(alt==1) {
                        urlString = RENDER_URL + urlName + "/" + alt + ".png";
                    }
                }
                break;
        }
        return urlString;
    }
}
