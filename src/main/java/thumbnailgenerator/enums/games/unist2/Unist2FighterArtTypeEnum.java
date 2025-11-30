package thumbnailgenerator.enums.games.unist2;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum Unist2FighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;

    Unist2FighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }
}
