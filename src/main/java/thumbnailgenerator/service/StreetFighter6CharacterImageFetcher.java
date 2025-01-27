package thumbnailgenerator.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.GeneratedGraphic;

@Service
public class StreetFighter6CharacterImageFetcher extends CharacterImageFetcher {

    static String FIGHTERS_URL = "https://raw.githubusercontent.com/jonborg/" +
            "ThumbnailGeneratorCharacterImageRepository/" +
            "refs/heads/v3.1.0-beta2/" +
            "sf6/render/";

    @Override
    URL getOnlineUrl(Fighter fighter, GeneratedGraphic generatedGraphic)
            throws MalformedURLException {
    String urlString;
        urlString = FIGHTERS_URL + fighter.getUrlName() + "/1.png";
        return new URL(urlString);
    }
}
