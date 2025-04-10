package thumbnailgenerator.utils.enums;

import thumbnailgenerator.dto.FighterArtSettings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.json.read.FighterArtSettingsRead;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtType;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.StreetFighter6FighterArtType;
import thumbnailgenerator.enums.Tekken8FighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;

import java.util.List;
import java.util.stream.Collectors;

public class FighterArtTypeUtils {

    private FighterArtTypeUtils(){}

    public static FighterArtType getEnum(Game game, String value){
        switch (game) {
            case SSBU:
                return SmashUltimateFighterArtType.valueOf(value);
            case ROA2:
                return RivalsOfAether2FighterArtType.valueOf(value);
            case SF6:
                return StreetFighter6FighterArtType.valueOf(value);
            case TEKKEN8:
                return Tekken8FighterArtType.valueOf(value);
            default:
                throw new IllegalArgumentException("Unknown gameName: " + game);
        }
    }

    public static List<FighterArtSettings> convertArtSettings(List<FighterArtSettingsRead> fighterArtSettingsReadList, Game game){
        return fighterArtSettingsReadList.stream()
                .map(art ->
                        FighterArtSettings.builder()
                                .artType(FighterArtTypeUtils.getEnum(game, art.getArtType()))
                                .fighterImageSettingsPath(art.getFighterImageSettingsPath())
                                .build()
                )
                .collect(Collectors.toList());
    }
}
