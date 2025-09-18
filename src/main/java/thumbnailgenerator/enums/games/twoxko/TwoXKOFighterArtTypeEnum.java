package thumbnailgenerator.enums.games.twoxko;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum TwoXKOFighterArtTypeEnum implements FighterArtTypeEnum {
    RENDER("Renders");

    private String value;

    TwoXKOFighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }
}
