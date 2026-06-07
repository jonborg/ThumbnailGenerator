package thumbnailgenerator.enums.games.ffcotw;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum FatalFuryCotwEnum implements CharacterEnum {

    ROCK("Rock Howard", "rock", 2692, 1),
    TERRY("Terry Bogard", "terry", 2694, 1),
    JENET("B. Jenet", "jenet", 2680, 1),
    MARCO("Marco Rodrigues", "marco", 2690, 1),
    PREECHA("Preecha", "preecha", 2691, 1),
    HOTARU("Hotaru Futaba", "hotaru", 2685, 1),
    VOX("Vox Reaper", "vox", 2696, 1),
    TIZOC("Tizoc", "tizoc", 2695, 1),
    KEVIN("Kevin Rian", "kevin", 2687, 1),
    BILLY("Billy Kane", "billy", 2681, 1),
    MAI("Mai Shiranui", "mai", 2689, 1),
    KIM_HWAN("Kim Dong Hwan", "kim_hwan", 2688, 1),
    GATO("Gato", "gato", 2683, 1),
    KAIN("Kain R. Heinlein", "kain", 2686, 1),
    CR7("Cristiano Ronaldo", "cr7", 2682, 1),
    GANACCI("Salvatore Ganacci", "ganacci", 2693, 1),
    HOKUTO("Hokuto Maru", "hokuto", 2684, 1),

    ANDY("Andy Bogard", "andy", 2710, 1),
    KEN("Ken", "ken", 2802, 1),
    JOE("Joe Higashi", "joe", 2801, 1),
    CHUN_LI("Chun-Li", "chunli", 2810, 1),
    MR_BIG("Mr.Big", "mr_big", 2826, 1),

    KIM_HOON("Kim Jae Hoon", "kim_hoon", 2947, 1),
    GEESE("Nigthmare Geese", "geese", 2948,1),
    BLUE_MARY("Blue Mary", "blue_mary", 2949,1),
    KRAUSER("Wolfgang Krauser", "krauser", 2963,1),
    MR_KARATE("Mr. Karate", "mr_karate", -1,1),

    RANDOM("Random", "random", 2800, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    FatalFuryCotwEnum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
