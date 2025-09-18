package thumbnailgenerator.service.games;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class TwoXKOCharacterImageFetcher extends CharacterImageFetcher {

    @Value("${character-image.2xko.render.path}")
    private String renderUrl;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtTypeEnum artType, boolean backup)
            throws MalformedURLException {
        String urlString = renderUrl + "/" + fighter.getUrlName() + "/1.png";
        return new URL(getHostAndBranchVersion(backup) + urlString);
    }
}
