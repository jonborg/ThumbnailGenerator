package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum SmashUltimateEnum implements CharacterEnum {

    //64
    MARIO("Mario", "mario", 1302, 8),
    DONKEY_KONG("Donkey Kong", "donkey_kong", 1280, 8),
    LINK("Link", "link", 1296, 8),
    SAMUS("Samus", "samus", 1328, 8),
    YOSHI("Yoshi", "yoshi", 1338, 8),
    KIRBY("Kirby", "kirby", 1295, 8),
    FOX("Fox", "fox", 1286, 8),
    PIKACHU("Pikachu", "pikachu", 1319, 8),
    LUIGI("Luigi", "luigi", 1301, 8),
    NESS("Ness", "ness", 1313, 8),
    CAPTAIN_FALCON("Captain Falcon", "captain_falcon", 1274, 8),
    JIGGLYPUFF("Jigglypuff", "jigglypuff", 1293, 8),

    // Melee
    PEACH("Peach", "peach", 1317, 8),
    BOWSER("Bowser", "bowser", 1273, 8),
    ICE_CLIMBERS("Ice Climbers", "ice_climbers", 1290, 8),
    SHEIK("Sheik", "sheik", 1329, 8),
    ZELDA("Zelda", "zelda", 1340, 8),
    DR_MARIO("Dr. Mario", "dr_mario", 1282, 8),
    PICHU("Pichu", "pichu", 1318, 8),
    FALCO("Falco", "falco", 1285, 8),
    MARTH("Marth", "marth", 1304, 8),
    YOUNG_LINK("Young Link", "young_link", 1339, 8),
    GANONDORF("Ganondorf", "ganondorf", 1287, 8),
    MEWTWO("Mewtwo", "mewtwo", 1310, 8),
    ROY("Roy", "roy", 1326, 8),
    MR_GAME_AND_WATCH("Mr. Game & Watch", "mr_game_and_watch", 1405, 8),

    // Brawl
    META_KNIGHT("Meta Knight", "meta_knight", 1307, 8),
    PIT("Pit", "pit", 1320, 8),
    ZERO_SUIT_SAMUS("Zero Suit Samus", "zero_suit_samus", 1341, 8),
    WARIO("Wario", "wario", 1335, 8),
    SNAKE("Snake", "snake", 1331, 8),
    IKE("Ike", "ike", 1291, 8),
    POKEMON_TRAINER("Pokémon Trainer", "pokemon_trainer", 1321, 8),
    DIDDY_KONG("Diddy Kong", "diddy_kong", 1279, 8),
    LUCAS("Lucas", "lucas", 1299, 8),
    SONIC("Sonic", "sonic", 1332, 8),
    KING_DEDEDE("King Dedede", "king_dedede", 1294, 8),
    OLIMAR("Olimar", "olimar", 1314, 8),
    LUCARIO("Lucario", "lucario", 1298, 8),
    ROB("R.O.B.", "rob", 1323, 8),
    TOON_LINK("Toon Link", "toon_link", 1333, 8),
    WOLF("Wolf", "wolf", 1337, 8),

    // 4
    VILLAGER("Villager", "villager", 1334, 8),
    MEGA_MAN("Mega Man", "mega_man", 1305, 8),
    WII_FIT_TRAINER("Wii Fit Trainer", "wii_fit_trainer", 1336, 8),
    ROSALINA_AND_LUMA("Rosalina & Luma", "rosalina_and_luma", 1325, 8),
    LITTLE_MAC("Little Mac", "little_mac", 1297, 8),
    GRENINJA("Greninja", "greninja", 1289, 8),
    MII_BRAWLER("Mii Brawler", "mii_brawler", 1311, 1),
    MII_SWORDFIGHTER("Mii Swordfighter", "mii_swordfighter", 1414, 1),
    MII_GUNNER("Mii Gunner", "mii_gunner", 1415, 2),
    PALUTENA("Palutena", "palutena", 1316, 8),
    PAC_MAN("Pac-Man", "pac_man", 1315, 8),
    ROBIN("Robin", "robin", 1324, 8),
    SHULK("Shulk", "shulk", 1330, 8),
    BOWSER_JR("Bowser Jr.", "bowser_jr", 1272, 8),
    DUCK_HUNT("Duck Hunt Duo", "duck_hunt", 1283, 8),
    RYU("Ryu", "ryu", 1327, 8),
    CLOUD("Cloud", "cloud", 1275, 8),
    CORRIN("Corrin", "corrin", 1276, 8),
    BAYONETTA("Bayonetta", "bayonetta", 1271, 8),

    // Ultimate
    INKLING("Inkling", "inkling", 1292, 8),
    RIDLEY("Ridley", "ridley", 1322, 8),
    SIMON("Simon", "simon", 1411, 8),
    KING_K_ROOL("King K. Rool", "king_k_rool", 1407, 8),
    ISABELLE("Isabelle", "isabelle", 1413, 8),
    INCINEROAR("Incineroar", "incineroar", 1406, 8),
    PIRANHA_PLANT("Piranha Plant", "piranha_plant", 1441, 8),
    JOKER("Joker", "joker", 1453, 8),
    DQ_HERO("Hero", "dq_hero", 1526, 8),
    BANJO_AND_KAZOOIE("Banjo & Kazooie", "banjo_and_kazooie", 1530, 8),
    TERRY("Terry", "terry", 1532, 8),
    BYLETH("Byleth", "byleth", 1539, 8),
    MINMIN("Min Min", "minmin", 1747, 8),
    STEVE("Steve", "steve", 1766, 8),
    SEPHIROTH("Sephiroth", "sephiroth", 1777, 8),
    PYRA("Pyra & Mythra", "pyra", 1795, 8),
    KAZUYA("Kazuya", "kazuya", 1846, 8),
    SORA("Sora", "sora", 1897, 8),

    // Echoes
    DARK_PIT("Dark Pit", "dark_pit", 1278, 8),
    LUCINA("Lucina", "lucina", 1300, 8),
    DAISY("Daisy", "daisy", 1277, 8),
    CHROM("Chrom", "chrom", 1409, 8),
    DARK_SAMUS("Dark Samus", "dark_samus", 1408, 8),
    RICHTER("Richter", "richter", 1412, 8),
    KEN("Ken", "ken", 1410, 8),

    RANDOM("Random", "random", 1749, 2);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    SmashUltimateEnum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
