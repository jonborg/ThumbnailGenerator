package thumbnailgenerator.exception;

public class CharacterImageSettingsNotFoundException extends Exception {
    public CharacterImageSettingsNotFoundException(String urlCharacter){
        super("Could not find image settings for fighter "+ urlCharacter);
    }
}
