package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum GranblueVersusRisingFighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;

    GranblueVersusRisingFighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }
}
