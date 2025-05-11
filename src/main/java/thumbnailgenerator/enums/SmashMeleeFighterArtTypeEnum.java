package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum SmashMeleeFighterArtTypeEnum implements FighterArtTypeEnum {
    HD("HD Renders");

    private String value;

    SmashMeleeFighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }

}
