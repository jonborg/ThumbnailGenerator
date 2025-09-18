package thumbnailgenerator.enums.games.ssb64r;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
public enum Smash64RemixFighterArtTypeEnum implements FighterArtTypeEnum {
    ARTWORK("Artworks");

    private String value;

    Smash64RemixFighterArtTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return this.name().toUpperCase();
    }

}
