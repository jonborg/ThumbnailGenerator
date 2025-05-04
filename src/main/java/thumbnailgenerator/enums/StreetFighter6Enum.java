package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum StreetFighter6Enum implements CharacterEnum {

    RYU("Ryu", "ryu", 2287),
    LUKE("Luke", "luke", 2284),
    JAMIE("Jamie", "jamie", 2278),
    CHUN_LI("Chun-Li", "chunli", 2273),
    GUILE("Guile", "guile", 2277),
    KIMBERLY("Kimberly", "kimberly", 2282),
    JURI("Juri", "juri", 2280),
    KEN("Ken", "ken", 2281),
    BLANKA("Blanka", "blanka", 2271),
    DHALSIM("Dhalsim", "dhalsim", 2275),
    E_HONDA("E.Honda", "ehonda", 2276),
    DEE_JAY("Dee Jay", "deejay", 2274),
    MANON("Manon", "manon", 2285),
    MARISA("Marisa", "marisa", 2286),
    JP("JP", "jp", 2279),
    ZANGIEF("Zangief", "zangief", 2288),
    LILY("Lily", "lily", 2283),
    CAMMY("Cammy", "cammy", 2272),
    RASHID("Rashid", "rashid", 2314),
    A_K_I("A.K.I.", "aki", 2342),
    ED("Ed", "ed", 2442),
    AKUMA("Akuma", "akuma", 2495),
    M_BISON("M. Bison", "m_bison", 2506),
    TERRY("Terry", "terry", 2596),
    MAI("Mai", "mai", 2616);

    private final String name;
    private final String code;
    private final int startGGId;

    StreetFighter6Enum(String name, String code, int startGGId) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
    }
}
