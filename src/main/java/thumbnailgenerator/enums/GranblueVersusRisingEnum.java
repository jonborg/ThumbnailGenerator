package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum GranblueVersusRisingEnum implements CharacterEnum {

    //Start.gg has no IDs for GBFVR...
    GRAN("Gran", "gran", -1, 1),
    DJEETA("Djeeta", "djeeta", -1, 1),
    KATALINA("Katalina", "katalina", -1, 1),
    CHARLOTTA("Charlotta", "charlotta", -1, 1),
    LANCELOT("Lancelot", "lancelot", -1, 1),
    PERCIVAL("Percival", "percival", -1, 1),
    LADIVA("Ladiva", "ladiva", -1, 1),
    METERA("Metera", "metera", -1, 1),
    LOWAIN("Lowain", "lowain", -1, 1),
    FERRY("Ferry", "ferry", -1, 1),
    ZETA("Zeta", "zeta", -1, 1),
    VASERAGA("Vaseraga", "vaseraga", -1, 1),
    NARMAYA("Narmaya", "narmaya", -1, 1),
    SORIZ("Soriz", "soriz", -1, 1),
    ZOOEY("Zooey", "zooey", -1, 1),
    CAGLIOSTRO("Cagliostro", "cagliostro", -1, 1),
    YUEL("Yuel", "yuel", -1, 1),
    ANRE("Anre", "anre", -1, 1),
    EUSTACE("Eustace", "eustace", -1, 1),
    SEOX("Seox", "seox", -1, 1),
    VIRA("Vira", "vira", -1, 1),
    BEELZEBUB("Beelzebub", "beelzebub", -1, 1),
    BELIAL("Belial", "belial", -1, 1),
    AVATAR_BELIAL("Avatar Belial", "avatar_belial", -1, 1),
    LUNALU("Lunalu", "lunalu", -1, 1),
    ANILA("Anila", "anila", -1, 1),
    SIEGFRIED("Siegfried", "siegfried", -1, 1),
    GRIMNIR("Grimnir", "grimnir", -1, 1),
    NIER("Nier", "nier", -1, 1),
    LUCILIUS("Lucilius", "lucilius", -1, 1),
    TWOB("2B", "2b", -1, 1),
    VANE("Vane", "vane", -1, 1),
    BEATRIX("Beatrix", "beatrix", -1, 1),
    VERSUSIA("Versusia", "versusia", -1, 1),
    VIKALA("Vikala", "vikala", -1, 1),
    SANDALPHON("Sandalphon", "sandalphon", -1, 1);
    //GALLEON("Galleon", "galleon", -1, 1);

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
