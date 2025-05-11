package thumbnailgenerator.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Service
public class StreetFighter6CharacterImageFetcher extends CharacterImageFetcher {

    @Value("${character-image.sf6.render.path}")
    private String renderUrl;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtTypeEnum artType, boolean backup)
            throws MalformedURLException {
        String urlString = renderUrl + "/" + fighter.getUrlName() + "/1.png";
        return new URL(getHostAndBranchVersion(backup) + urlString);
    }
}
