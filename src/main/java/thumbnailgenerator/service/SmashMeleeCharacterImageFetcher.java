package thumbnailgenerator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.enums.interfaces.FighterArtType;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class SmashMeleeCharacterImageFetcher extends CharacterImageFetcher {

    @Value("${character-image.ssbm.hd.url}")
    private String renderUrl;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtType artType)
            throws MalformedURLException {
        String urlString = renderUrl + "/"
                + fighter.getUrlName() + "/"
                + fighter.getAlt() + ".png";
        return new URL(urlString);
    }
}
