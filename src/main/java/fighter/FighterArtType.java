package fighter;

import lombok.Getter;

@Getter
public enum FighterArtType {
    RENDER("Renders"),
    MURAL("Mural Arts");

    private String name;

    private FighterArtType(String name) {
        this.name = name;
    }
}
