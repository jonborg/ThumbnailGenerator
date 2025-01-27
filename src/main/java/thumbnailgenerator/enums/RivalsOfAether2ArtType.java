package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
public enum RivalsOfAether2ArtType implements FighterArtType {
    RENDER("Renders");

    private String name;
    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/ro2/default.json";

    RivalsOfAether2ArtType(String name) {
        this.name = name;
    }

    @Override
    public String getDefaultFighterImageSettingsFile() {
        return defaultRenderImageSettingsFile;
    }
}
