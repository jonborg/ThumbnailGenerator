package fighter;

import lombok.Getter;

@Getter
public enum SmashUltimateFighterArtType {
    RENDER("Renders"),
    MURAL("Mural Arts");

    private String name;

    private SmashUltimateFighterArtType(String name) {
        this.name = name;
    }
}
