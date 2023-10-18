package thumbnailgenerator.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.GraphicGenerated;

@Service
public class StreetFighter6CharacterImageFetcher extends CharacterImageFetcher {

    static String FIGHTERS_URL = "https://www.streetfighter.com/6/assets/images/character/";

    @Override
    URL getOnlineUrl(Fighter fighter, GraphicGenerated graphicGenerated)
            throws MalformedURLException {
    String urlString;
        urlString = FIGHTERS_URL + fighter.getUrlName() + "/" +fighter.getAlt() +".png";
        return new URL(urlString);
    }
}
