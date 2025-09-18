package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.games.twoxko.TwoXKOEnum;
import thumbnailgenerator.enums.games.twoxko.TwoXKOFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.games.CharacterImageFetcher;
import thumbnailgenerator.service.games.TwoXKOCharacterImageFetcher;

@Component
public class TwoXKOEnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.2xko.render.default}")
    private String defaultRenderImageSettingsFile;
    @Autowired
    private TwoXKOCharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.TWOXKO;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return TwoXKOEnum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return TwoXKOFighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return TwoXKOFighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
