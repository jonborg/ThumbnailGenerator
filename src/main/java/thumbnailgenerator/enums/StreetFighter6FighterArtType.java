package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
public enum StreetFighter6FighterArtType implements FighterArtType {
    RENDER("Renders");

    private String name;
    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/sf6/default.json";


    StreetFighter6FighterArtType(String name) {
        this.name = name;
    }

    @Override
    public String getDefaultFighterImageSettingsFile() {
        return defaultRenderImageSettingsFile;
    }
}
