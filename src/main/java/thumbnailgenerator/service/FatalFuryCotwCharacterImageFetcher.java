package thumbnailgenerator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class FatalFuryCotwCharacterImageFetcher extends CharacterImageFetcher {

    @Value("${character-image.ffcotw.render.url}")
    private String renderUrl;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtTypeEnum artType)
            throws MalformedURLException {
    String urlString;
        urlString = renderUrl + "/" + fighter.getUrlName() + "/1.png";
        return new URL(urlString);
    }
}
