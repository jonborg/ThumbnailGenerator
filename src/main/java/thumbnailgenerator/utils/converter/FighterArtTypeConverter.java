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

        if (object instanceof SmashUltimateFighterArtTypeEnum) {
            SmashUltimateFighterArtTypeEnum
                    artType = (SmashUltimateFighterArtTypeEnum) object;
            return artType.getValue();
        }
        if (object instanceof RivalsOfAether2FighterArtTypeEnum) {
            RivalsOfAether2FighterArtTypeEnum
                    artType = (RivalsOfAether2FighterArtTypeEnum) object;
            return artType.getValue();
        }
        if (object instanceof StreetFighter6FighterArtTypeEnum) {
            StreetFighter6FighterArtTypeEnum
                    artType = (StreetFighter6FighterArtTypeEnum) object;
            return artType.getValue();
        }
        if (object instanceof Tekken8FighterArtTypeEnum) {
            Tekken8FighterArtTypeEnum artType = (Tekken8FighterArtTypeEnum) object;
            return artType.getValue();
        }
        if (object instanceof FatalFuryCotwFighterArtTypeEnum) {
            FatalFuryCotwFighterArtTypeEnum
                    artType = (FatalFuryCotwFighterArtTypeEnum) object;
            return artType.getValue();
        }
        return null;
    }

    @Override
    public FighterArtTypeEnum fromString(String string) {
        return SmashUltimateFighterArtTypeEnum.valueOf(string);
    }
}
