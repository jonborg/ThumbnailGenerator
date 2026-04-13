package thumbnailgenerator.enums.games.unist2;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum Unist2Enum implements CharacterEnum {

    HYDE("Hyde", "hyde", 2726,1),
    LINNE("Linne", "linne", 2729, 1),
    WALDSTEIN("Waldstein", "waldstein", 2742, 1),
    CARMINE("Carmine", "carmine", 2720, 1),
    ORIE("Orie", "orie", 2735, 1),
    GORDEAU("Gordeau", "gordeau", 2724, 1),
    MERKAVA("Merkava", "merkava", 2731, 1),
    VATISTA("Vatista", "vatista", 2740, 1),
    SETH("Seth", "seth", 2737, 1),
    YUZURIHA("Yuzuriha", "yuzuriha", 2743, 1),
    HILDA("Hilda", "hilda", 2725, 1),
    CHAOS("Chaos", "chaos", 2721, 1),

    NANASE("Nanase", "nanase", 2733, 1),
    BYAKUYA("Byakuya", "byakuya", 2719, 1),
    PHONON("Phonon", "phonon", 2736, 1),
    MIKA("Mika", "mika", 2732, 1),

    WAGNER("Wagner", "wagner", 2741, 1),
    ENKIDU("Enkidu", "enkidu", 2723, 1),

    LONDREKIA("Londrekia", "londrekia", 2730, 1),

    TSURUGI("Tsurugi", "tsurugi", 2738, 1),
    KAGUYA("Kaguya", "kaguya", 2727, 1),
    KUON("Kuon", "kuon", 2728, 1),

    UZUKI("Uzuki", "uzuki", 2739, 1),
    OGRE("Ogre", "ogre", 2734, 1),
    IZUMI("Izumi", "izumi", 2827, 1),

    ELTNUM("Eltnum", "eltnum", 2722, 1),
    AKATSUKI("Akatsuki", "akatsuki", 2718, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    Unist2Enum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
