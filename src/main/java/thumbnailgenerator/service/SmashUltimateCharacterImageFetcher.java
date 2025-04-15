package thumbnailgenerator.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.GeneratedGraphic;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Service
public class SmashUltimateCharacterImageFetcher extends CharacterImageFetcher {

    static String FIGHTERS_URL = "https://www.smashbros.com/assets_v2/img/fighter/";
    static String FIGHTERS_URL_2 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/renders/";
    static String SANS_URL = "https://i.redd.it/n2tcplon8qk31.png";
    static String MURAL_URL = "https://raw.githubusercontent.com/jonborg/" +
            "ThumbnailGeneratorCharacterImageRepository/" +
            "refs/heads/v3.1.0-beta4/" +
            "ssbu/mural/";
    static String RENDER_URL = "https://raw.githubusercontent.com/jonborg/" +
            "ThumbnailGeneratorCharacterImageRepository/" +
            "refs/heads/v3.1.0-beta4/" +
            "ssbu/render/";

    @Override
    public URL getOnlineUrl(Fighter fighter, FighterArtType artType)
            throws MalformedURLException {
        String urlString;
        switch ((SmashUltimateFighterArtType) artType) {
            case MURAL:
                urlString = MURAL_URL + fighter.getUrlName() + "/" + fighter.getAlt() + ".png";
                break;
            case RENDER:
            default:
                if (fighter.getAlt() == 1) {
                    urlString = FIGHTERS_URL + fighter.getUrlName() + "/main.png";
                } else {
                    urlString = FIGHTERS_URL + fighter.getUrlName() + "/main" + fighter.getAlt() + ".png";
                }

                if (fighter.getUrlName().contains(SmashUltimateEnum.POKEMON_TRAINER.getCode())) {
                    urlString =
                            FIGHTERS_URL_2 + "misc/pokemon-trainer-0" + fighter.getAlt() +
                                    ".png";
                }
                if (fighter.getUrlName().contains(SmashUltimateEnum.MII_BRAWLER.getCode())) {
                    urlString = FIGHTERS_URL_2 + "fighters/51/01.png";
                }
                if (fighter.getUrlName().contains(SmashUltimateEnum.MII_SWORDFIGHTER.getCode())) {
                    urlString = FIGHTERS_URL_2 + "fighters/52/01.png";
                }
                if (fighter.getUrlName().contains(SmashUltimateEnum.MII_GUNNER.getCode())) {
                    if (fighter.getAlt() == 1)
                        urlString = FIGHTERS_URL_2 + "fighters/53/01.png";
                    if (fighter.getAlt() == 2) urlString = SANS_URL;
                }
                if (fighter.getUrlName().contains("random")) {
                    urlString = RENDER_URL + fighter.getUrlName() + "/" + fighter.getAlt() + ".png";
                }
                break;
        }
        return new URL(urlString);    }
}
