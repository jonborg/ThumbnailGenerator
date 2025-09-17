package thumbnailgenerator.utils.converter;

import javafx.util.StringConverter;
import thumbnailgenerator.enums.games.ssbu.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

public class FighterArtTypeConverter extends StringConverter<FighterArtTypeEnum> {

    @Override
    public String toString(FighterArtTypeEnum object) {
        return object == null ? null : object.getValue();
    }

    @Override
    public FighterArtTypeEnum fromString(String string) {
        return SmashUltimateFighterArtTypeEnum.valueOf(string);
    }
}
