package fighter;

import exception.OnlineImageNotFoundException;
import file.FileUtils;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class StreetFighter6DownloadFighterURL implements DownloadFighterURL<SmashUltimateFighterArtType> {
    private static final Logger LOGGER = LogManager.getLogger(
            StreetFighter6DownloadFighterURL.class);

    static String FIGHTERS_URL = "https://www.streetfighter.com/6/assets/images/character/";

    public String generateFighterURL(String urlName, int alt, SmashUltimateFighterArtType artType) {
        var path = FileUtils.getLocalFightersPath(SmashUltimateFighterArtType.RENDER);
        var fighterPath = System.getProperty("user.dir") + path + urlName + "/" + alt +".png";
        var file = new File(fighterPath);
        if (!file.exists()){
            LOGGER.debug("Image for {}{} does not exist locally. Will try finding online", urlName, alt);
            return getOnlineURL(urlName,alt);
        }
        return fighterPath;
    }

    public BufferedImage getFighterImageOnline(Fighter fighter, SmashUltimateFighterArtType artType) throws
            OnlineImageNotFoundException {
        try {
            URL url = new URL(getOnlineURL(fighter.getUrlName(), fighter.getAlt()));
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
            urlConnection.setRequestProperty("Referrer Policy", "strict-origin-when-cross-origin");
            LOGGER.debug("Trying to find image online for alt {} of {}: {}", fighter.getAlt(), fighter.getName(),url.getHost() + url.getPath());
            InputStream inputStream = url.openConnection().getInputStream();
            return ImageIO.read(inputStream);
        }catch(IOException e){
            LOGGER.error("An issue occurred when finding image for alt {} of {}. URI: {}",
                    fighter.getAlt(), fighter.getName(), getOnlineURL(fighter.getUrlName(), fighter.getAlt()));
            LOGGER.catching(e);
            throw new OnlineImageNotFoundException(e.getMessage());
        }
    }

    private String getOnlineURL(String urlName, int alt) {
        String urlString;
        urlString = FIGHTERS_URL + urlName + "/" +urlName +".png";
        return urlString;
    }
}
