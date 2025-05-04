package thumbnailgenerator.enums;

import lombok.Getter;

@Getter
public enum SmashMeleeEnum {

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
    MR_GAME_AND_WATCH("Mr. Game & Watch", "mr_game_and_watch", 1405);

    private final String name;
    private final String code;
    private final int startGGId;

    SmashMeleeEnum(String name, String code, int startGGId) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
    }
}
