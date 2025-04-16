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
import java.util.Map;
import java.util.stream.Collectors;

public class FighterArtTypeUtils {

    private FighterArtTypeUtils(){}

    private static final Map<Game, Class<? extends FighterArtType>> gameArtTypesMap = Map.of(
            Game.SSBU, SmashUltimateFighterArtType.class,
            Game.ROA2, RivalsOfAether2FighterArtType.class,
            Game.SF6, StreetFighter6FighterArtType.class,
            Game.TEKKEN8, Tekken8FighterArtType.class
    );

    private static final Map<Game, Enum<? extends FighterArtType>> gameDefaultArtTypeMap = Map.of(
            Game.SSBU, SmashUltimateFighterArtType.RENDER,
            Game.ROA2, RivalsOfAether2FighterArtType.RENDER,
            Game.SF6, StreetFighter6FighterArtType.RENDER,
            Game.TEKKEN8, Tekken8FighterArtType.RENDER
    );

    public static FighterArtType getEnum(Game game, String value) {
        Class<? extends FighterArtType> enumClass = gameArtTypesMap.get(game);
        if (enumClass == null) {
            throw new IllegalArgumentException("Unknown gameName: " + game);
        }
        return (FighterArtType) Enum.valueOf((Class) enumClass, value.toUpperCase());
    }

    public static FighterArtType[] getValues(Game game) {
        Class<? extends FighterArtType> enumClass = gameArtTypesMap.get(game);
        if (enumClass == null) {
            throw new IllegalArgumentException("Unknown gameName: " + game);
        }
        return enumClass.getEnumConstants();
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

    public static FighterArtType getDefaultArtType(Game game){
        return getEnum(game, gameDefaultArtTypeMap.get(game).name());
    }
}
