package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.GranblueVersusRisingEnum;
import thumbnailgenerator.enums.GranblueVersusRisingFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.GranblueVersusRisingCharacterImageFetcher;

@Component
public class GranblueVersusRisingEnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.gbfvr.render.default}")
    private String defaultRenderImageSettingsFile;
    @Autowired
    private GranblueVersusRisingCharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.GBFVR;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return GranblueVersusRisingEnum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return GranblueVersusRisingFighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return GranblueVersusRisingFighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
