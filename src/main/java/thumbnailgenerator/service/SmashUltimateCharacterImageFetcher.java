package thumbnailgenerator.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Service
public class SmashUltimateCharacterImageFetcher extends CharacterImageFetcher {

    @Value("${character-image.ssbu.render.url}")
    private String renderUrl;

    @Value("${character-image.ssbu.mural.url}")
    private String muralUrl;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtTypeEnum artType)
            throws MalformedURLException {
        String partialUrl = artType.equals(SmashUltimateFighterArtTypeEnum.MURAL) ?
                muralUrl : renderUrl;
        String urlString = partialUrl + "/" + fighter.getUrlName() + "/" + fighter.getAlt() +".png";
        return new URL(urlString);
    }
}
