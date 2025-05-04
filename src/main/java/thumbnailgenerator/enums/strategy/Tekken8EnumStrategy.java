package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.Tekken8Enum;
import thumbnailgenerator.enums.Tekken8FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.Tekken8CharacterImageFetcher;

@Component
public class Tekken8EnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.tekken8.render.default}")
    private static String defaultRenderImageSettingsFile;
    @Autowired
    private Tekken8CharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.TEKKEN8;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return Tekken8Enum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return Tekken8FighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return Tekken8FighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
