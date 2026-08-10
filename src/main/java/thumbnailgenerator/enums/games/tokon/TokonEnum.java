package thumbnailgenerator.enums.games.tokon;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum TokonEnum implements CharacterEnum {
    CAPTAIN_AMERICA("Captain America", "captain_america", -1, 1),
    IRON_MAN("Iron Man", "iron_man", -1, 1),
    BLACK_PANTHER("Black Panther", "black_panther", -1, 1),
    HULK("Hulk", "hulk", -1, 1),

    STORM("Storm", "storm", -1, 1),
    MAGIK("Magik", "magik", -1, 1),
    WOLVERINE("Wolverine", "wolverine", -1, 1),
    DANGER("Danger", "danger", -1, 1),

    SPIDER_MAN("Spider-Man", "spider_man", -1, 1),
    MS_MARVEL("Ms. Marvel", "ms_marvel", -1, 1),
    STAR_LORD("Star-Lord", "star_lord", -1, 1),
    PENI("Peni Parker", "peni", -1, 1),

    GHOST_RIDER("Ghost Rider", "ghost_rider", -1, 1),
    BLADE("Blade", "blade", -1, 1),
    LOKI("Loki", "loki", -1, 1),
    DEADPOOL("Deadpool", "deadpool", -1, 1),

    DOCTOR_DOOM("Doctor Doom", "doctor_doom", -1, 1),
    MAGNETO("Magneto", "magneto", -1, 1),
    GREEN_GOBLIN("Green Goblin", "green_goblin", -1, 1),
    CARNAGE("Carnage", "carnage", -1, 1),

    //CHAMPION("Champion", "champion", -1, 1),

    RANDOM("Random", "random", -1, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    TokonEnum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
