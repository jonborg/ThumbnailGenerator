package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.SmashMeleeEnum;
import thumbnailgenerator.enums.SmashMeleeFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.SmashMeleeCharacterImageFetcher;

@Component
public class SmashMeleeEnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.ssbm.hd.default}")
    private String defaultHDImageSettingsFile;
    @Autowired
    private SmashMeleeCharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.SSBM;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return SmashMeleeEnum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return SmashMeleeFighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }
    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return SmashMeleeFighterArtTypeEnum.HD;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultHDImageSettingsFile;
    }
}
