package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
public enum SmashUltimateFighterArtType implements FighterArtType {
    RENDER("Renders"),
    MURAL("Mural Arts");

    private String value;
    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/default.json";
    private static String defaultMuralImageSettingsFile= "settings/thumbnails/images/defaultMural.json";

    SmashUltimateFighterArtType(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }

    @Override
    public String getDefaultFighterImageSettingsFile() {
        return this == MURAL ? defaultMuralImageSettingsFile : defaultRenderImageSettingsFile;
    }
}
