package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum StreetFighter6Enum implements CharacterEnum {

    RYU("Ryu", "ryu", 2287, 1),
    LUKE("Luke", "luke", 2284, 1),
    JAMIE("Jamie", "jamie", 2278, 1),
    CHUN_LI("Chun-Li", "chunli", 2273, 1),
    GUILE("Guile", "guile", 2277, 1),
    KIMBERLY("Kimberly", "kimberly", 2282, 1),
    JURI("Juri", "juri", 2280, 1),
    KEN("Ken", "ken", 2281, 1),
    BLANKA("Blanka", "blanka", 2271, 1),
    DHALSIM("Dhalsim", "dhalsim", 2275, 1),
    E_HONDA("E.Honda", "ehonda", 2276, 1),
    DEE_JAY("Dee Jay", "deejay", 2274, 1),
    MANON("Manon", "manon", 2285, 1),
    MARISA("Marisa", "marisa", 2286, 1),
    JP("JP", "jp", 2279, 1),
    ZANGIEF("Zangief", "zangief", 2288, 1),
    LILY("Lily", "lily", 2283, 1),
    CAMMY("Cammy", "cammy", 2272, 1),
    RASHID("Rashid", "rashid", 2314, 1),
    A_K_I("A.K.I.", "aki", 2342, 1),
    ED("Ed", "ed", 2442, 1),
    AKUMA("Akuma", "akuma", 2495, 1),
    M_BISON("M. Bison", "m_bison", 2506, 1),
    TERRY("Terry", "terry", 2596, 1),
    MAI("Mai", "mai", 2616, 1),
    ELENA("Elena", "elena", 2699, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    StreetFighter6Enum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
