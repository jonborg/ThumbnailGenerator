package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum RivalsOfAether2FighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;

    RivalsOfAether2FighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }

}
