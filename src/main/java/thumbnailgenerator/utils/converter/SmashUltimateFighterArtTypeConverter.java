package thumbnailgenerator.utils.converter;

import javafx.util.StringConverter;
import thumbnailgenerator.service.SmashUltimateFighterArtType;

public class SmashUltimateFighterArtTypeConverter extends StringConverter<SmashUltimateFighterArtType> {
    @Override
    public String toString(SmashUltimateFighterArtType object) {
        return object.getName();
    }

    @Override
    public SmashUltimateFighterArtType fromString(String string) {
        return SmashUltimateFighterArtType.valueOf(string);
    }
}
