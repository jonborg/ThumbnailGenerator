package thumbnailgenerator.enums.games.twoxko;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum TwoXKOEnum implements CharacterEnum {

    AHRI("Ahri", "ahri", 2714, 1),
    YASUO("Yasuo", "yasuo", 2715, 1),
    DARIUS("Darius", "darius", 2717, 1),
    EKKO("Ekko", "ekko", 2713, 1),
    ILLAOI("Illaoi", "illaoi", 2716, 1),
    BRAUM("Braum", "braum", 2711, 1),
    JINX("Jinx", "jinx", 2712, 1),
    VI("Vi", "vi", 2788, 1),
    BLITZCRANK("Blitzcrank", "blitzcrank", 2789, 1),
    TEEMO("Teemo", "teemo", 2796, 1),
    WARWICK("Warwick", "warwick", 2795, 1),

    CAITLYN("Caitlyn", "caitlyn", 2945, 1),
    AKALI("Akali", "akali", -1, 1),
    SENNA("Senna", "senna", 3206, 1),
    THRESH("Thresh", "thresh", 3205, 1),

    RANDOM("Random", "random", 2799, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    TwoXKOEnum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
