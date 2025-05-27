package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum Smash64RemixEnum implements CharacterEnum {

    //64
    MARIO("Mario", "mario", 85, 8),
    DONKEY_KONG("Donkey Kong", "donkey_kong", 86, 8),
    LINK("Link", "link", 87, 7),
    SAMUS("Samus", "samus", 88, 8),
    YOSHI("Yoshi", "yoshi", 89, 8),
    KIRBY("Kirby", "kirby", 90, 7),
    FOX("Fox", "fox", 91, 8),
    PIKACHU("Pikachu", "pikachu", 92, 8),
    LUIGI("Luigi", "luigi", 93, 7),
    NESS("Ness", "ness", 95, 7),
    CAPTAIN_FALCON("Captain Falcon", "captain_falcon", 94, 8),
    JIGGLYPUFF("Jigglypuff", "jigglypuff", 96, 8),

    //REMIX - no start.gg
    GANONDORF("Ganondorf", "ganondorf", -1, 7),
    YOUNG_LINK("Young Link", "young_link", -1, 7),
    FALCO("Falco", "falco", -1, 6),
    DR_MARIO("Dr. Mario", "dr_mario", -1, 6),
    DARK_SAMUS("Dark Samus", "dark_samus", -1, 7),
    WARIO("Wario", "wario", -1, 6),
    LUCAS("Lucas", "lucas", -1, 6),
    BOWSER("Bowser", "bowser", -1, 6),
    WOLF("Wolf", "wolf", -1, 7),
    CONKER("Conker", "conker", -1, 7),
    MEWTWO("Mewtwo", "mewtwo", -1, 6),
    MARTH("Marth", "marth", -1, 6),
    SONIC("Sonic", "sonic", -1, 6),
    CLASSIC_SONIC("Classic Sonic", "classic_sonic", -1, 6),
    SHEIK("Sheik", "sheik", -1, 7),
    MARINA("Marina", "marina", -1, 6),
    KING_DEDEDE("King Dedede", "king_dedede", -1, 6),
    GOEMON("Goemon", "goemon", -1, 7),
    BANJO_AND_KAZOOIE("Banjo & Kazooie", "banjo_and_kazooie", -1, 7);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    Smash64RemixEnum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
