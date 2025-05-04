package thumbnailgenerator.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.service.CharacterImageFetcher;
import thumbnailgenerator.service.RivalsOfAether2CharacterImageFetcher;
import thumbnailgenerator.service.SmashMeleeCharacterImageFetcher;
import thumbnailgenerator.service.SmashUltimateCharacterImageFetcher;
import thumbnailgenerator.service.StreetFighter6CharacterImageFetcher;
import thumbnailgenerator.service.Tekken8CharacterImageFetcher;

@Component
public class CharacterImageFetcherFactory {

    @Autowired
    private SmashUltimateCharacterImageFetcher smashUltimateCharacterImageFetcher;
    @Autowired
    private SmashMeleeCharacterImageFetcher smashMeleeCharacterImageFetcher;
    @Autowired
    private StreetFighter6CharacterImageFetcher streetFighter6CharacterImageFetcher;
    @Autowired
    private RivalsOfAether2CharacterImageFetcher rivalsOfAether2CharacterImageFetcher;
    @Autowired
    private Tekken8CharacterImageFetcher tekken8CharacterImageFetcher;

    public CharacterImageFetcher getCharacterImageFetcher(Game game) {
        switch (game) {
            case ROA2:
                return rivalsOfAether2CharacterImageFetcher;
            case SF6:
                return streetFighter6CharacterImageFetcher;
            case SSBU:
                return smashUltimateCharacterImageFetcher;
            case SSBM:
                return smashMeleeCharacterImageFetcher;
            case TEKKEN8:
                return tekken8CharacterImageFetcher;
            default:
                throw new IllegalArgumentException("Unsupported service type: " + game);
        }
    }
}