package thumbnailgenerator.enums.interfaces;

import thumbnailgenerator.dto.Game;
import thumbnailgenerator.service.CharacterImageFetcher;

public interface GameEnumStrategy {
    Game getGame();
    Class<? extends CharacterEnum> getCharacterEnumClass();
    Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass();
    CharacterImageFetcher getImageFetcher();

    FighterArtTypeEnum getDefaultFighterArtTypeEnumClass();
    String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum);
}
