package thumbnailgenerator.service;

import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.GeneratedGraphic;
import thumbnailgenerator.enums.SmashUltimateEnum;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class RivalsOfAether2CharacterImageFetcher extends CharacterImageFetcher {

    static String RENDER_URL =
            "https://raw.githubusercontent.com/jonborg/" +
                    "ThumbnailGeneratorCharacterImageRepository/" +
                    "refs/heads/v3.1.0-beta1/" +
                    "roa2/render/";

    @Override
    URL getOnlineUrl(Fighter fighter, GeneratedGraphic generatedGraphic)
            throws MalformedURLException {
        String urlString = new StringBuffer()
                .append(RENDER_URL)
                .append(fighter.getUrlName())
                .append("/1.png")
                .toString();
        return new URL(urlString);
    }
}
