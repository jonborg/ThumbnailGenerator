package thumbnailgenerator.exception;

public class FighterImageSettingsNotFoundException extends Exception {
    public FighterImageSettingsNotFoundException(String fighterUrl){
        super("Could not find image settings for fighter "+ fighterUrl);
    }
}
