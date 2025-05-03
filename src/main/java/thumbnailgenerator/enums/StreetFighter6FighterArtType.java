package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
public enum StreetFighter6FighterArtType implements FighterArtType {
    RENDER("Renders");

    private String value;
    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/sf6/default.json";

    StreetFighter6FighterArtType(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }

    @Override
    public String getDefaultFighterImageSettingsFile() {
        return defaultRenderImageSettingsFile;
    }
}
