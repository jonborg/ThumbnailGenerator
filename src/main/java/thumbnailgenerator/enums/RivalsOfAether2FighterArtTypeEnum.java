package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum RivalsOfAether2FighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;
    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/roa2/default.json";

    RivalsOfAether2FighterArtTypeEnum(String value) {
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
