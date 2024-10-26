package thumbnailgenerator.enums;

import lombok.Getter;

@Getter
public enum SmashUltimateEnum {

    //64
    MARIO("Mario", "mario", 1302),
    DONKEY_KONG("Donkey Kong", "donkey_kong", 1280),
    LINK("Link", "link", 1296),
    SAMUS("Samus", "samus", 1328),
    YOSHI("Yoshi", "yoshi", 1338),
    KIRBY("Kirby", "kirby", 1295),
    FOX("Fox", "fox", 1286),
    PIKACHU("Pikachu", "pikachu", 1319),
    LUIGI("Luigi", "luigi", 1301),
    NESS("Ness", "ness", 1313),
    CAPTAIN_FALCON("Captain Falcon", "captain_falcon", 1274),
    JIGGLYPUFF("Jigglypuff", "jigglypuff", 1293),

    // Melee
    PEACH("Peach", "peach", 1317),
    BOWSER("Bowser", "bowser", 1273),
    ICE_CLIMBERS("Ice Climbers", "ice_climbers", 1290),
    SHEIK("Sheik", "sheik", 1329),
    ZELDA("Zelda", "zelda", 1340),
    DR_MARIO("Dr. Mario", "dr_mario", 1282),
    PICHU("Pichu", "pichu", 1318),
    FALCO("Falco", "falco", 1285),
    MARTH("Marth", "marth", 1304),
    YOUNG_LINK("Young Link", "young_link", 1339),
    GANONDORF("Ganondorf", "ganondorf", 1287),
    MEWTWO("Mewtwo", "mewtwo", 1310),
    ROY("Roy", "roy", 1326),
    MR_GAME_AND_WATCH("Mr. Game & Watch", "mr_game_and_watch", 1405),

    // Brawl
    META_KNIGHT("Meta Knight", "meta_knight", 1307),
    PIT("Pit", "pit", 1320),
    ZERO_SUIT_SAMUS("Zero Suit Samus", "zero_suit_samus", 1341),
    WARIO("Wario", "wario", 1335),
    SNAKE("Snake", "snake", 1331),
    IKE("Ike", "ike", 1291),
    POKEMON_TRAINER("Pokémon Trainer", "pokemon_trainer", 1321),
    DIDDY_KONG("Diddy Kong", "diddy_kong", 1279),
    LUCAS("Lucas", "lucas", 1299),
    SONIC("Sonic", "sonic", 1332),
    KING_DEDEDE("King Dedede", "king_dedede", 1294),
    OLIMAR("Olimar", "olimar", 1314),
    LUCARIO("Lucario", "lucario", 1298),
    ROB("R.O.B.", "rob", 1323),
    TOON_LINK("Toon Link", "toon_link", 1333),
    WOLF("Wolf", "wolf", 1337),

    // 4
    VILLAGER("Villager", "villager", 1334),
    MEGA_MAN("Mega Man", "mega_man", 1305),
    WII_FIT_TRAINER("Wii Fit Trainer", "wii_fit_trainer", 1336),
    ROSALINA_AND_LUMA("Rosalina & Luma", "rosalina_and_luma", 1325),
    LITTLE_MAC("Little Mac", "little_mac", 1297),
    GRENINJA("Greninja", "greninja", 1289),
    MII_BRAWLER("Mii Brawler", "mii_brawler", 1311),
    MII_SWORDFIGHTER("Mii Swordfighter", "mii_swordfighter", 1414),
    MII_GUNNER("Mii Gunner", "mii_gunner", 1415),
    PALUTENA("Palutena", "palutena", 1316),
    PAC_MAN("Pac-Man", "pac_man", 1315),
    ROBIN("Robin", "robin", 1324),
    SHULK("Shulk", "shulk", 1330),
    BOWSER_JR("Bowser Jr.", "bowser_jr", 1272),
    DUCK_HUNT("Duck Hunt Duo", "duck_hunt", 1283),
    RYU("Ryu", "ryu", 1327),
    CLOUD("Cloud", "cloud", 1275),
    CORRIN("Corrin", "corrin", 1276),
    BAYONETTA("Bayonetta", "bayonetta", 1271),

    // Ultimate
    INKLING("Inkling", "inkling", 1292),
    RIDLEY("Ridley", "ridley", 1322),
    SIMON("Simon", "simon", 1411),
    KING_K_ROOL("King K. Rool", "king_k_rool", 1407),
    ISABELLE("Isabelle", "isabelle", 1413),
    INCINEROAR("Incineroar", "incineroar", 1406),
    PIRANHA_PLANT("Piranha Plant", "piranha_plant", 1441),
    JOKER("Joker", "joker", 1453),
    DQ_HERO("Hero", "dq_hero", 1526),
    BANJO_AND_KAZOOIE("Banjo & Kazooie", "banjo_and_kazooie", 1530),
    TERRY("Terry", "terry", 1532),
    BYLETH("Byleth", "byleth", 1539),
    MINMIN("Min Min", "minmin", 1747),
    STEVE("Steve", "steve", 1766),
    SEPHIROTH("Sephiroth", "sephiroth", 1777),
    PYRA("Pyra & Mythra", "pyra", 1795),
    KAZUYA("Kazuya", "kazuya", 1846),
    SORA("Sora", "sora", 1897),

    // Echoes
    DARK_PIT("Dark Pit", "dark_pit", 1278),
    LUCINA("Lucina", "lucina", 1300),
    DAISY("Daisy", "daisy", 1277),
    CHROM("Chrom", "chrom", 1409),
    DARK_SAMUS("Dark Samus", "dark_samus", 1408),
    RICHTER("Richter", "richter", 1412),
    KEN("Ken", "ken", 1410),

    RANDOM("Random", "random", 1749);

    private final String name;
    private final String code;
    private final int startggId;

    SmashUltimateEnum(String name, String code, int startggId) {
        this.name = name;
        this.code = code;
        this.startggId = startggId;
    }
}
