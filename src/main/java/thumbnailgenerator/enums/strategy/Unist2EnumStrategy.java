package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.games.unist2.Unist2Enum;
import thumbnailgenerator.enums.games.unist2.Unist2FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.games.CharacterImageFetcher;
import thumbnailgenerator.service.games.Unist2CharacterImageFetcher;

@Component
public class Unist2EnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.unist2.render.default}")
    private String defaultRenderImageSettingsFile;
    @Autowired
    private Unist2CharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.UNIST2;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return Unist2Enum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return Unist2FighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return Unist2FighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
