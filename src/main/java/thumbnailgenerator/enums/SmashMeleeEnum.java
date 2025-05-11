package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum SmashMeleeEnum implements CharacterEnum {

    //64
    MARIO("Mario", "mario", 13, 5),
    DONKEY_KONG("Donkey Kong", "donkey_kong", 3, 5),
    LINK("Link", "link", 11, 5),
    SAMUS("Samus", "samus", 22, 5),
    YOSHI("Yoshi", "yoshi", 24, 6),
    KIRBY("Kirby", "kirby", 10, 6),
    FOX("Fox", "fox", 6, 4),
    PIKACHU("Pikachu", "pikachu", 20, 4),
    LUIGI("Luigi", "luigi", 12, 4),
    NESS("Ness", "ness", 17, 4),
    CAPTAIN_FALCON("Captain Falcon", "captain_falcon", 2, 6),
    JIGGLYPUFF("Jigglypuff", "jigglypuff", 9, 5),

    // Melee
    PEACH("Peach", "peach", 18, 5),
    BOWSER("Bowser", "bowser", 1, 4),
    ICE_CLIMBERS("Ice Climbers", "ice_climbers", 8, 4),
    SHEIK("Sheik", "sheik", 23, 5),
    ZELDA("Zelda", "zelda", 26, 5),
    DR_MARIO("Dr. Mario", "dr_mario", 4, 5),
    PICHU("Pichu", "pichu", 19, 4),
    FALCO("Falco", "falco", 5, 4),
    MARTH("Marth", "marth", 14, 5),
    YOUNG_LINK("Young Link", "young_link", 25, 5),
    GANONDORF("Ganondorf", "ganondorf", 7, 5),
    MEWTWO("Mewtwo", "mewtwo", 15, 4),
    ROY("Roy", "roy", 21, 5),
    MR_GAME_AND_WATCH("Mr. Game & Watch", "mr_game_and_watch", 16, 4),

    RANDOM("Random", "random", 1744, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    SmashMeleeEnum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
