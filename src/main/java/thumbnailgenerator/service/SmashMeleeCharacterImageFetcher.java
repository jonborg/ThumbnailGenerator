package thumbnailgenerator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class SmashMeleeCharacterImageFetcher extends CharacterImageFetcher {

    @Value("${character-image.ssbm.hd.path}")
    private String hdPath;

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtTypeEnum artType, boolean backup)
            throws MalformedURLException {
        String urlString = hdPath + "/" + fighter.getUrlName() + "/" + fighter.getAlt() +".png";
        return new URL(getHostAndBranchVersion(backup) + urlString);
    }
}
