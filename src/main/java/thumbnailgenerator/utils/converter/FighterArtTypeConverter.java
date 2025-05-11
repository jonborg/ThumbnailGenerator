package thumbnailgenerator.utils.converter;

import javafx.util.StringConverter;
import thumbnailgenerator.enums.FatalFuryCotwFighterArtTypeEnum;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtTypeEnum;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.StreetFighter6FighterArtTypeEnum;
import thumbnailgenerator.enums.Tekken8FighterArtTypeEnum;
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
