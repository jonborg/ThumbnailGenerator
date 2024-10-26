package thumbnailgenerator.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.RivalsOfAether2Enum;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.RivalsOfAether2CharacterImageFetcher;
import thumbnailgenerator.service.SmashUltimateCharacterImageFetcher;
import thumbnailgenerator.service.StreetFighter6CharacterImageFetcher;

@Component
public class CharacterImageFetcherFactory {

    @Autowired
    private SmashUltimateCharacterImageFetcher smashUltimateCharacterImageFetcher;
    @Autowired
    private StreetFighter6CharacterImageFetcher streetFighter6CharacterImageFetcher;
    @Autowired
    private RivalsOfAether2CharacterImageFetcher rivalsOfAether2CharacterImageFetcher;

    public CharacterImageFetcher getCharacterImageFetcher(Game game) {
        switch (game) {
            case ROA2:
                return rivalsOfAether2CharacterImageFetcher;
            case SF6:
                return streetFighter6CharacterImageFetcher;
            case SSBU:
                return smashUltimateCharacterImageFetcher;
            default:
                throw new IllegalArgumentException("Unsupported service type: " + game);
        }
    }
}