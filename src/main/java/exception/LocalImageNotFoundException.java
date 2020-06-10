package exception;

import java.io.FileNotFoundException;

public class LocalImageNotFoundException extends FileNotFoundException {
    public LocalImageNotFoundException (String pathname){
        super("Could not find image in "+ pathname);

    }
}
