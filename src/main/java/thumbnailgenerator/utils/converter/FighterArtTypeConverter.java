package thumbnailgenerator.utils.converter;

import javafx.util.StringConverter;
import thumbnailgenerator.enums.RivalsOfAether2ArtType;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.StreetFighter6FighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;

public class FighterArtTypeConverter extends StringConverter<FighterArtType> {

    @Override
    public String toString(FighterArtType object) {

        if (object instanceof SmashUltimateFighterArtType) {
            SmashUltimateFighterArtType smashArtType = (SmashUltimateFighterArtType) object;
            return smashArtType.getName();
        }
        if (object instanceof RivalsOfAether2ArtType) {
            RivalsOfAether2ArtType smashArtType = (RivalsOfAether2ArtType) object;
            return smashArtType.getName();
        }
        if (object instanceof StreetFighter6FighterArtType) {
            StreetFighter6FighterArtType smashArtType = (StreetFighter6FighterArtType) object;
            return smashArtType.getName();
        }
        return null;
    }

    @Override
    public SmashUltimateFighterArtType fromString(String string) {
        return SmashUltimateFighterArtType.valueOf(string);
    }
}
