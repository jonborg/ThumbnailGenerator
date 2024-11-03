package thumbnailgenerator.utils.enums;

import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.RivalsOfAether2Enum;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.StreetFighter6Enum;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartGGEnumUtils {

    public static <E extends Enum<E>> String findCodeByName(Class<E> enumClass, String name) {
        return findValue(enumClass, String.valueOf(name), "getName");
    }

    public static <E extends Enum<E>> String findCodeByStartggId(int startGGId) {
        String value = findValue(SmashUltimateEnum.class, String.valueOf(startGGId), "getStartGGId");
        if (value != null) return value;
        value = findValue(StreetFighter6Enum.class, String.valueOf(startGGId), "getStartGGId");
        if (value != null) return value;
        value = findValue(RivalsOfAether2Enum.class, String.valueOf(startGGId), "getStartGGId");
        return value;
    }

    public static Game findGameByStartGGId(int startGGId) {
        return Arrays.stream(Game.values())
                .filter(g -> g.getStartGGId() == startGGId)
                .findFirst()
                .get();
    }

    private static <E extends Enum<E>> String findValue(Class<E> enumClass, String searchParam, String methodName) {
        for (E value : enumClass.getEnumConstants()) {
            try {
                String paramValue =  String.valueOf(enumClass.getMethod(methodName).invoke(value));
                if (paramValue.equalsIgnoreCase(searchParam)) {
                    return (String) enumClass.getMethod("getCode").invoke(value);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <E extends Enum<E>> List<String> getAllNames(Class<E> enumClass) {
        List<String> names = new ArrayList<>();
        try {
            for (E value : enumClass.getEnumConstants()) {
                names.add((String) enumClass.getMethod("getName").invoke(value));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return names;
    }
}
