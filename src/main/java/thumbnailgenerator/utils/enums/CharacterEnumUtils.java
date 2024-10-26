package thumbnailgenerator.utils.enums;

import thumbnailgenerator.enums.RivalsOfAether2Enum;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.StreetFighter6Enum;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CharacterEnumUtils {

    public static <E extends Enum<E>> String findCodeByName(Class<E> enumClass, String name) {
        return findValue(enumClass, String.valueOf(name), "getName");
    }

    public static <E extends Enum<E>> String findCodeByStartggId(int startggId) {
        String value = findValue(SmashUltimateEnum.class, String.valueOf(startggId), "getStartggId");
        if (value != null) return value;
        value = findValue(StreetFighter6Enum.class, String.valueOf(startggId), "getStartggId");
        if (value != null) return value;
        value = findValue(RivalsOfAether2Enum.class, String.valueOf(startggId), "getStartggId");
        return value;
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
