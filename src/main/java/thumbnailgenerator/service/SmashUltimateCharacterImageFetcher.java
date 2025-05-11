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

    @Value("${character-image.ssbu.render.path}")
    private String renderPath;

    @Value("${character-image.ssbu.mural.path}")
    private String muralPath;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtTypeEnum artType, boolean backup)
            throws MalformedURLException {
        String partialPath = artType.equals(SmashUltimateFighterArtTypeEnum.MURAL) ?
                muralPath : renderPath;
        String urlString = partialPath + "/" + fighter.getUrlName() + "/" + fighter.getAlt() +".png";
        return new URL(getHostAndBranchVersion(backup) + urlString);
    }
}
