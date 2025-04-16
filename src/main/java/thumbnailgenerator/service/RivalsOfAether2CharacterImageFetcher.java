package thumbnailgenerator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.GeneratedGraphic;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.interfaces.FighterArtType;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class RivalsOfAether2CharacterImageFetcher extends CharacterImageFetcher {

    @Value("${character-image.roa2.render.url}")
    private String renderUrl;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtType artType)
            throws MalformedURLException {
        String urlString = renderUrl + "/" + fighter.getUrlName() + "/1.png";
        return new URL(urlString);
    }
}
