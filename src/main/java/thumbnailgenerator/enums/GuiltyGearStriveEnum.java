package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum GuiltyGearStriveEnum implements CharacterEnum {

    SOL("Sol Badguy", "sol", 1841, 1),
    KY("Ky Kiske", "ky", 1834, 1),
    MAY("May", "may", 1836, 1),
    AXL("Axl Low", "axl", 1829, 1),
    CHIPP("Chipp Zanuff", "chipp", 1830, 1),
    POTEMKIN("Potemkin", "potemkin", 1839, 1),
    FAUST("Faust", "faust", 1831, 1),
    MILLIA("Millia Rage", "millia", 1837, 1),
    ZATO("Zato=1", "zato", 1842, 1),
    RAMLETHAL("Ramlethal Valentine", "ramlethal", 1840, 1),
    LEO("Leo Whitefang", "leo", 1835, 1),
    NAGORIYUKI("Nagoriyuki", "nagoriyuki", 1838, 1),
    GIOVANNA("Giovanna", "giovanna", 1832, 1),
    ANJI("Anji", "anji", 1828, 1),
    INO("I-No", "ino", 1833, 1),

    GOLDLEWIS("Goldlewis Dickinson", "goldlewis", 1849, 1),
    JACK_O("Jack-O'", "jack_o", 1872, 1),
    HAPPY_CHAOS("Happy Chaos", "happy_chaos", 1913, 1),
    BAIKEN("Baiken", "baiken", 1952, 1),
    TESTAMENT("Testament", "testament", 1951, 1),

    BRIDGET("Bridget", "faust", 2097, 1),
    SIN("Sin Kiske", "sin", 2316, 1),
    BEDMAN("Bedman?", "bedman", 2448, 1),
    ASUKA("Asuka R #", "asuka", 2315, 1),

    JOHNNY("Johnny", "johnny", 2318, 1),
    ELPHET("Elphet Valentine", "elphet", 2404, 1),
    ABA("A.B.A", "aba", 2445, 1),
    SLAYER("Slayer", "slayer", 2496, 1),

    DIZZY("Dizzy", "dizzy", 2613, 1),
    VENOM("Venom", "venom", 2622, 1),
    UNIKA("Unika", "unika", 2744, 1),

    RANDOM("Random", "random", 2621, 1);


    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    GuiltyGearStriveEnum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
