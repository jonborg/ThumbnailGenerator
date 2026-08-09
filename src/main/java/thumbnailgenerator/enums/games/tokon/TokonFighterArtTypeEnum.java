package thumbnailgenerator.enums.games.tokon;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum TokonFighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;

    TokonFighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }
}
