package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.RivalsOfAether2Enum;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.RivalsOfAether2CharacterImageFetcher;

@Component
public class RivalsOfAether2EnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.roa2.render.default}")
    private String defaultRenderImageSettingsFile;
    @Autowired
    private RivalsOfAether2CharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.ROA2;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return RivalsOfAether2Enum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return RivalsOfAether2FighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return RivalsOfAether2FighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
