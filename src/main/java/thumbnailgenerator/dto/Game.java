package thumbnailgenerator.dto;

import lombok.Getter;

@Getter
public enum Game {
    SSBU("ssbu","Smash Ultimate"),
    ROA2("roa2","Rivals of Aether 2"),
    SF6("sf6", "Street Fighter 6");

    private String code;
    private String name;

    Game(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
