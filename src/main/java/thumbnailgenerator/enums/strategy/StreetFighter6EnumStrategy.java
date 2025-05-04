package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.StreetFighter6Enum;
import thumbnailgenerator.enums.StreetFighter6FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.StreetFighter6CharacterImageFetcher;

@Component
public class StreetFighter6EnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.sf6.render.default}")
    private static String defaultRenderImageSettingsFile;
    @Autowired
    private StreetFighter6CharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.SF6;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return StreetFighter6Enum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return StreetFighter6FighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return StreetFighter6FighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
