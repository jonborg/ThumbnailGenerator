package thumbnailgenerator.dto;

import lombok.Getter;

@Getter
public enum Game {
    SSBU("Smash Ultimate"),
    SF6("Street Fighter 6");

    private String name;

    private Game(String name) {
        this.name = name;
    }
}
