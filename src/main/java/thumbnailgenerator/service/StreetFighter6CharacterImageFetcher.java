package thumbnailgenerator.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Service
public class StreetFighter6CharacterImageFetcher extends CharacterImageFetcher {

    @Value("${character-image.sf6.render.url}")
    private String renderUrl;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtTypeEnum artType)
            throws MalformedURLException {
    String urlString;
        urlString = renderUrl + "/" + fighter.getUrlName() + "/1.png";
        return new URL(urlString);
    }
}
