package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
public enum Tekken8FighterArtType implements FighterArtType {
    RENDER("Renders");

    private String name;
    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/tekken8/default.json";


    Tekken8FighterArtType(String name) {
        this.name = name;
    }

    @Override
    public String getDefaultFighterImageSettingsFile() {
        return defaultRenderImageSettingsFile;
    }
}
