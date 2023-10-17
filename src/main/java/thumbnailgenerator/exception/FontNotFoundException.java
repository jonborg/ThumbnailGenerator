package thumbnailgenerator.exception;

public class FontNotFoundException extends Exception {
    public FontNotFoundException(String fontName){
        super("Could not find font "+ fontName + " on program's resources.");

    }
}
