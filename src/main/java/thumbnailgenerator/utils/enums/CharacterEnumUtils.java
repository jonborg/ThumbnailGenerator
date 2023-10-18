package thumbnailgenerator.utils.enums;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CharacterEnumUtils {

    public static <E extends Enum<E>> String findCodeByName(Class<E> enumClass, String name) {
        return findValue(enumClass, String.valueOf(name), "getName");
    }

    public static <E extends Enum<E>> String findCodeByStartggId(Class<E> enumClass, int startggId) {
        return findValue(enumClass, String.valueOf(startggId), "getStartggId");
    }

    private static <E extends Enum<E>> String findValue(Class<E> enumClass, String searchParam, String methodName) {
        for (E value : enumClass.getEnumConstants()) {
            try {
                String paramValue = (String) enumClass.getMethod(methodName).invoke(value);
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
