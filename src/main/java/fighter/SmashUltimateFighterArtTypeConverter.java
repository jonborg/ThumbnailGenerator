package fighter;

import javafx.util.StringConverter;

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
