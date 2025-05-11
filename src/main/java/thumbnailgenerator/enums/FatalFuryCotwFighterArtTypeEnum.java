package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum FatalFuryCotwFighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;

    FatalFuryCotwFighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }
}
