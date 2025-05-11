package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.SmashUltimateCharacterImageFetcher;

@Component
public class SmashUltimateEnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.ssbu.render.default}")
    private String defaultRenderImageSettingsFile;
    @Value("${thumbnail.settings.ssbu.mural.default}")
    private String defaultMuralImageSettingsFile;
    @Autowired
    private SmashUltimateCharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.SSBU;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return SmashUltimateEnum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return SmashUltimateFighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }
    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return SmashUltimateFighterArtTypeEnum.RENDER;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return fighterArtTypeEnum == SmashUltimateFighterArtTypeEnum.MURAL ?
                defaultMuralImageSettingsFile : defaultRenderImageSettingsFile;
    }
}
