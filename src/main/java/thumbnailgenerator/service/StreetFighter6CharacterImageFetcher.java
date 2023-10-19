package thumbnailgenerator.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.GeneratedGraphic;

@Service
public class StreetFighter6CharacterImageFetcher extends CharacterImageFetcher {

    static String FIGHTERS_URL = "https://www.streetfighter.com/6/assets/images/character/";

    @Override
    URL getOnlineUrl(Fighter fighter, GeneratedGraphic generatedGraphic)
            throws MalformedURLException {
    String urlString;
        urlString = FIGHTERS_URL + fighter.getUrlName() + "/" +fighter.getUrlName() +".png";
        return new URL(urlString);
    }
}
