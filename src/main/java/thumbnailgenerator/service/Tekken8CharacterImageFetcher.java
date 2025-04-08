package thumbnailgenerator.service;

import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.GeneratedGraphic;
import thumbnailgenerator.enums.interfaces.FighterArtType;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class Tekken8CharacterImageFetcher extends CharacterImageFetcher {

    static String RENDER_URL =
            "https://raw.githubusercontent.com/jonborg/" +
                    "ThumbnailGeneratorCharacterImageRepository/" +
                    "refs/heads/v3.1.0-beta4/" +
                    "tekken8/";

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtType artType)
            throws MalformedURLException {
        String urlString = RENDER_URL +
                artType.toString().toLowerCase() + "/" +
                fighter.getUrlName() +
                "/1.png";
        return new URL(urlString);
    }
}
