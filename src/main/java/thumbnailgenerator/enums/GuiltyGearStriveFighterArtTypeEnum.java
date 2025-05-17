package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum GuiltyGearStriveFighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;

    GuiltyGearStriveFighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }
}
