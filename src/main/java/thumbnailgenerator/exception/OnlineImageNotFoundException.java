package thumbnailgenerator.exception;

import java.io.IOException;

public class OnlineImageNotFoundException extends IOException {

    public OnlineImageNotFoundException () {
        super();
    }
    public OnlineImageNotFoundException (String urlFighter){
        super("Could not find image online with the following link: " + urlFighter);
    }
}
