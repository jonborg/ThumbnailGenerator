package thumbnailgenerator.utils.converter;

import javafx.util.StringConverter;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtType;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.StreetFighter6FighterArtType;
import thumbnailgenerator.enums.Tekken8FighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;

public class FighterArtTypeConverter extends StringConverter<FighterArtType> {

    @Override
    public String toString(FighterArtType object) {

        if (object instanceof SmashUltimateFighterArtType) {
            SmashUltimateFighterArtType artType = (SmashUltimateFighterArtType) object;
            return artType.getValue();
        }
        if (object instanceof RivalsOfAether2FighterArtType) {
            RivalsOfAether2FighterArtType artType = (RivalsOfAether2FighterArtType) object;
            return artType.getValue();
        }
        if (object instanceof StreetFighter6FighterArtType) {
            StreetFighter6FighterArtType artType = (StreetFighter6FighterArtType) object;
            return artType.getValue();
        }
        if (object instanceof Tekken8FighterArtType) {
            Tekken8FighterArtType artType = (Tekken8FighterArtType) object;
            return artType.getValue();
        }
        return null;
    }

    @Override
    public FighterArtType fromString(String string) {
        return SmashUltimateFighterArtType.valueOf(string);
    }
}
