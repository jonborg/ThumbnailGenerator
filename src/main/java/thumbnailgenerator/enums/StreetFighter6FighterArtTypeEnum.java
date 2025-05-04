package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum StreetFighter6FighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;
    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/sf6/default.json";

    StreetFighter6FighterArtTypeEnum(String value) {
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
