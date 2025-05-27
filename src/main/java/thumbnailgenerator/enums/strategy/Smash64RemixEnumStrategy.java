package thumbnailgenerator.enums.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.Smash64RemixEnum;
import thumbnailgenerator.enums.Smash64RemixFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.Smash64RemixCharacterImageFetcher;

@Component
public class Smash64RemixEnumStrategy implements GameEnumStrategy {

    @Value("${thumbnail.settings.ssb64r.artwork.default}")
    private String defaultArtworkImageSettingsFile;
    @Autowired
    private Smash64RemixCharacterImageFetcher fetcher;

    @Override
    public Game getGame() {
        return Game.SSB64R;
    }

    @Override
    public Class<? extends CharacterEnum> getCharacterEnumClass() {
        return Smash64RemixEnum.class;
    }

    @Override
    public Class<? extends FighterArtTypeEnum> getFighterArtTypeEnumClass() {
        return Smash64RemixFighterArtTypeEnum.class;
    }

    @Override
    public CharacterImageFetcher getImageFetcher() {
        return fetcher;
    }
    @Override
    public FighterArtTypeEnum getDefaultFighterArtTypeEnumClass() {
        return Smash64RemixFighterArtTypeEnum.ARTWORK;
    }

    @Override
    public String getDefaultFighterImageSettingsFile(FighterArtTypeEnum fighterArtTypeEnum) {
        return defaultArtworkImageSettingsFile;
    }
}
