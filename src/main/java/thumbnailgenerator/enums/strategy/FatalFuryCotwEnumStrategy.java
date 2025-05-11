package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.FatalFuryCotwEnum;
import thumbnailgenerator.enums.FatalFuryCotwFighterArtTypeEnum;
import thumbnailgenerator.enums.Tekken8Enum;
import thumbnailgenerator.enums.Tekken8FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.FatalFuryCotwCharacterImageFetcher;
import thumbnailgenerator.service.Tekken8CharacterImageFetcher;

@Component
public class FatalFuryCotwEnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.ffcotw.render.default}")
    private String defaultRenderImageSettingsFile;
    @Autowired
    private FatalFuryCotwCharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.FFCOTW;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return FatalFuryCotwEnum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return FatalFuryCotwFighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return FatalFuryCotwFighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
