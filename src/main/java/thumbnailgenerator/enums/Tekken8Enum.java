package thumbnailgenerator.enums;

import lombok.Getter;

@Getter
public enum Tekken8Enum {

    ALISA("Alisa", "alisa", 2406),
    ASUKA("Asuka", "asuka", 2407),
    AZUCENA("Azucena", "azucena", 2408),
    BRYAN("Bryan", "bryan", 2409),
    CLAUDIO("Claudio", "claudio", 2410),
    DEVIL_JIN("Devil Jin", "devil_jin", 2411),
    FENG("Feng", "feng_wei", 2412),
    HWOARANG("Hwoarang", "hwoarang", 2413),
    JACK_8("Jack-8", "jack_8", 2414),
    JIN("Jin", "jin", 2415),
    JUN("Jun", "jun", 2416),
    KAZUYA("Kazuya", "kazuya", 2417),
    KING("King", "king", 2418),
    KUMA("Kuma", "kuma", 2419),
    LARS("Lars", "lars", 2420),
    LEE("Lee", "lee", 2421),
    LEO("Leo", "leo", 2422),
    LEROY("Leroy", "leroy", 2423),
    LILI("Lili", "lili", 2424),
    XIAOYU("Xiaoyu", "xiaoyu", 2425),
    LAW("Law", "law", 2426),
    NINA("Nina", "nina", 2427),
    PANDA("Panda", "panda", 2428),
    PAUL("Paul", "paul", 2429),
    RAVEN("Raven", "raven", 2431),
    REINA("Reina", "reina", 2432),
    DRAGUNOV("Dragunov", "dragunov", 2433),
    SHAHEEN("Shaheen", "shaheen", 2434),
    STEVE("Steve", "steve", 2435),
    VICTOR("Victor", "victor", 2436),
    YOSHIMITSU("Yoshimitsu", "yoshimitsu", 2437),
    ZAFINA("Zafina", "zafina", 2438),
    EDDY("Eddy", "eddy", 2446),
    LIDIA("Lidia", "lidia", 2538),
    HEIHACHI("Heihachi", "heihachi", 2598),
    CLIVE("Clive", "clive", 2612),
    ANNA("Anna", "anna", 2620);

    private final String name;
    private final String code;
    private final int startGGId;


    Tekken8Enum(String name, String code, int startGGId) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
    }
}
