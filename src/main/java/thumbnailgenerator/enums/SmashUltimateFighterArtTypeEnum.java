package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum SmashUltimateFighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders"),
    MURAL("Mural Arts");

    private String value;

    SmashUltimateFighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }
}
