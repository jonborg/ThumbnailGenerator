package fighter.name;

import javafx.util.StringConverter;

public class GameConverter extends StringConverter<Game> {
    @Override
    public String toString(Game object) {
        return object.getName();
    }

    @Override
    public Game fromString(String string) {
        return Game.valueOf(string);
    }
}
