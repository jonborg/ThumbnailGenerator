package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum GranblueVersusRisingEnum implements CharacterEnum {

    //Start.gg has no IDs for GBFVR...
    GRAN("Gran", "gran", 2760, 1),
    DJEETA("Djeeta", "djeeta", 2755, 1),
    KATALINA("Katalina", "katalina", 2762, 1),
    CHARLOTTA("Charlotta", "charlotta", 2756, 1),
    LANCELOT("Lancelot", "lancelot", 2764, 1),
    PERCIVAL("Percival", "percival", 2771, 1),
    LADIVA("Ladiva", "ladiva", 2763, 1),
    METERA("Metera", "metera", 2768, 1),
    LOWAIN("Lowain", "lowain", 2765, 1),
    FERRY("Ferry", "ferry", 2758, 1),
    ZETA("Zeta", "zeta", 2783, 1),
    VASERAGA("Vaseraga", "vaseraga", 2777, 1),
    NARMAYA("Narmaya", "narmaya", 2769, 1),
    SORIZ("Soriz", "soriz", 2775, 1),
    ZOOEY("Zooey", "zooey", 2784, 1),
    CAGLIOSTRO("Cagliostro", "cagliostro", 2754, 1),
    YUEL("Yuel", "yuel", 2782, 1),
    ANRE("Anre", "anre", 2749, 1),
    EUSTACE("Eustace", "eustace", 2757, 1),
    SEOX("Seox", "seox", 2773, 1),
    VIRA("Vira", "vira", 2780, 1),
    BEELZEBUB("Beelzebub", "beelzebub", 2752, 1),
    BELIAL("Belial", "belial", 2753, 1),
    AVATAR_BELIAL("Avatar Belial", "avatar_belial", 2750, 1),
    LUNALU("Lunalu", "lunalu", 2767, 1),
    ANILA("Anila", "anila", 2786, 1),
    SIEGFRIED("Siegfried", "siegfried", 2774, 1),
    GRIMNIR("Grimnir", "grimnir", 2761, 1),
    NIER("Nier", "nier", 2770, 1),
    LUCILIUS("Lucilius", "lucilius", 2766, 1),
    TWOB("2B", "2b", 2785, 1),
    VANE("Vane", "vane", 2776, 1),
    BEATRIX("Beatrix", "beatrix", 2751, 1),
    VERSUSIA("Versusia", "versusia", 2778, 1),
    VIKALA("Vikala", "vikala", 2779, 1),
    SANDALPHON("Sandalphon", "sandalphon", 2772, 1),
    GALLEON("Galleon", "galleon", 2759, 1),
    WILNAS("Wilnas", "wilnas", 2781, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    GranblueVersusRisingEnum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
