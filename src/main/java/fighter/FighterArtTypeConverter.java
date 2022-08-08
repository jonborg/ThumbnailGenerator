package fighter;

import javafx.util.StringConverter;

public class FighterArtTypeConverter extends StringConverter<FighterArtType> {
    @Override
    public String toString(FighterArtType object) {
        return object.getName();
    }

    @Override
    public FighterArtType fromString(String string) {
        return FighterArtType.valueOf(string);
    }
}
