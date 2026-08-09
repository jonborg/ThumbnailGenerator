package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.games.tokon.TokonEnum;
import thumbnailgenerator.enums.games.tokon.TokonFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.games.CharacterImageFetcher;
import thumbnailgenerator.service.games.TokonCharacterImageFetcher;

@Component
public class TokonEnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.tokon.render.default}")
    private String defaultRenderImageSettingsFile;
    @Autowired
    private TokonCharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.TOKON;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return TokonEnum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return TokonFighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return TokonFighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
