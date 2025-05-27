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
    JIGGLYPUFF("Jigglypuff", "jigglypuff", 96, 8);

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
