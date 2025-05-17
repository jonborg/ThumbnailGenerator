package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.GuiltyGearStriveEnum;
import thumbnailgenerator.enums.GuiltyGearStriveFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.GuiltyGearStriveCharacterImageFetcher;

@Component
public class GuiltyGearStriveEnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.ggst.render.default}")
    private String defaultRenderImageSettingsFile;
    @Autowired
    private GuiltyGearStriveCharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.GGST;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return GuiltyGearStriveEnum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return GuiltyGearStriveFighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }

    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return GuiltyGearStriveFighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultRenderImageSettingsFile;
    }
}
