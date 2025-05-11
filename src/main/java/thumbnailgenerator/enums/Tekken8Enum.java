package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum Tekken8Enum implements CharacterEnum {

    ALISA("Alisa", "alisa", 2406, 1),
    ASUKA("Asuka", "asuka", 2407, 1),
    AZUCENA("Azucena", "azucena", 2408, 1),
    BRYAN("Bryan", "bryan", 2409, 1),
    CLAUDIO("Claudio", "claudio", 2410, 1),
    DEVIL_JIN("Devil Jin", "devil_jin", 2411, 1),
    FENG("Feng", "feng_wei", 2412, 1),
    HWOARANG("Hwoarang", "hwoarang", 2413, 1),
    JACK_8("Jack-8", "jack_8", 2414, 1),
    JIN("Jin", "jin", 2415, 1),
    JUN("Jun", "jun", 2416, 1),
    KAZUYA("Kazuya", "kazuya", 2417, 1),
    KING("King", "king", 2418, 1),
    KUMA("Kuma", "kuma", 2419, 1),
    LARS("Lars", "lars", 2420, 1),
    LEE("Lee", "lee", 2421, 1),
    LEO("Leo", "leo", 2422, 1),
    LEROY("Leroy", "leroy", 2423, 1),
    LILI("Lili", "lili", 2424, 1),
    XIAOYU("Xiaoyu", "xiaoyu", 2425, 1),
    LAW("Law", "law", 2426, 1),
    NINA("Nina", "nina", 2427, 1),
    PANDA("Panda", "panda", 2428, 1),
    PAUL("Paul", "paul", 2429, 1),
    RAVEN("Raven", "raven", 2431, 1),
    REINA("Reina", "reina", 2432, 1),
    DRAGUNOV("Dragunov", "dragunov", 2433, 1),
    SHAHEEN("Shaheen", "shaheen", 2434, 1),
    STEVE("Steve", "steve", 2435, 1),
    VICTOR("Victor", "victor", 2436, 1),
    YOSHIMITSU("Yoshimitsu", "yoshimitsu", 2437, 1),
    ZAFINA("Zafina", "zafina", 2438, 1),
    EDDY("Eddy", "eddy", 2446, 1),
    LIDIA("Lidia", "lidia", 2538, 1),
    HEIHACHI("Heihachi", "heihachi", 2598, 1),
    CLIVE("Clive", "clive", 2612, 1),
    ANNA("Anna", "anna", 2620, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    Tekken8Enum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
