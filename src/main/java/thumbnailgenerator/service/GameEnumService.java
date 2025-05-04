package thumbnailgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.interfaces.CharacterEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.GameEnumStrategy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GameEnumService {

    private final Map<Game, GameEnumStrategy> strategyMap;

    @Autowired
    public GameEnumService(List<GameEnumStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(GameEnumStrategy::getGame, Function.identity()));
    }

    public Class<? extends CharacterEnum> getCharacterEnumClass(Game game) {
        return getStrategy(game).getCharacterEnumClass();
    }

    public Class<? extends FighterArtTypeEnum> getArtTypeEnumClass(Game game) {
        return getStrategy(game).getFighterArtTypeEnumClass();
    }

    public CharacterEnum[] getAllCharacters(Game game) {
        return getCharacterEnumClass(game).getEnumConstants();
    }

    public FighterArtTypeEnum[] getAllFighterArtTypes(Game game) {
        return getArtTypeEnumClass(game).getEnumConstants();
    }

    public CharacterEnum getCharacterEnum(Game game, String code) {
        for (CharacterEnum e : getAllCharacters(game)) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Character not found: " + code);
    }

    public FighterArtTypeEnum getArtTypeEnum(Game game, String code) {
        for (FighterArtTypeEnum e : getAllFighterArtTypes(game)) {
            if (e.getEnumName().equalsIgnoreCase(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Character not found: " + code);
    }

    public FighterArtTypeEnum getDefaultArtType(Game game){
        return getStrategy(game).getDefaultFighterArtTypeEnumClass();
    }

    public CharacterImageFetcher getCharacterImageFetcher(Game game) {
        return getStrategy(game).getImageFetcher();
    }

    public String findCharacterCodeByName(Game game, String searchParam) {
        var enumClass = getCharacterEnumClass(game);
        for (CharacterEnum value : getAllCharacters(game)) {
            try {
                String paramValue =  String.valueOf(enumClass.getMethod("getName").invoke(value));
                if (paramValue.equalsIgnoreCase(searchParam)) {
                    return (String) enumClass.getMethod("getCode").invoke(value);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<String> getAllCharacterNames(Game game) {
        List<String> names = new ArrayList<>();
        try {
            for (CharacterEnum value : getAllCharacters(game)) {
                names.add((String) getCharacterEnumClass(game)
                        .getMethod("getName")
                        .invoke(value));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return names;
    }

    private GameEnumStrategy getStrategy(Game game) {
        if (!strategyMap.containsKey(game)) {
            throw new IllegalArgumentException("No strategy found for game: " + game);
        }
        return strategyMap.get(game);
    }
}

