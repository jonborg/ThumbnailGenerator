package thumbnailgenerator.enums;

import lombok.Getter;

@Getter
public enum StreetFighter6Enum {

    RYU("Ryu", "ryu", 0),
    LUKE("Luke", "luke", 0),
    JAMIE("Jamie", "jamie", 0),
    CHUN_LI("Chun-Li", "chunli", 0),
    GUILE("Guile", "guile", 0),
    KIMBERLY("Kimberly", "kimberly", 0),
    JURI("Juri", "juri", 0),
    KEN("Ken", "ken", 0),
    BLANKA("Blanka", "blanka", 0),
    DHALSIM("Dhalsim", "dhalsim", 0),
    E_HONDA("E.Honda", "ehonda", 0),
    DEE_JAY("Dee Jay", "deejay", 0),
    MANON("Manon", "manon", 0),
    MARISA("Marisa", "marisa", 0),
    JP("JP", "jp", 0),
    ZANGIEF("Zangief", "zangief", 0),
    LILY("Lily", "lily", 0),
    CAMMY("Cammy", "cammy", 0),
    RASHID("Rashid", "rashid", 0),
    A_K_I("A.K.I.", "aki", 0),
    ED("Ed", "ed", 0),
    AKUMA("Akuma", "akuma", 0);

    private final String name;
    private final String code;
    private final int startggId;


    StreetFighter6Enum(String name, String code, int startggId) {
        this.name = name;
        this.code = code;
        this.startggId = startggId;
    }
}
