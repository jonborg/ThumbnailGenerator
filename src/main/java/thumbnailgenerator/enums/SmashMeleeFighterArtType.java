package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
public enum SmashMeleeFighterArtType implements FighterArtType {
    HD("HD");

    private String value;
    private static String defaultHDImageSettingsFile= "settings/thumbnails/images/ssbm/default.json";

    SmashMeleeFighterArtType(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }

    @Override
    public String getDefaultFighterImageSettingsFile() {
        return defaultHDImageSettingsFile;
    }
}
