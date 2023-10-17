package thumbnailgenerator.dto;

import lombok.Getter;

@Getter
public enum Game {
    SMASH_ULTIMATE("Smash Ultimate"),
    SF6("Street Fighter 6");

    private String name;

    private Game(String name) {
        this.name = name;
    }
}
