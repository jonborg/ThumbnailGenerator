package fighter;

import exception.OnlineImageNotFoundException;

import java.awt.image.BufferedImage;

public interface DownloadFighterURL<T> {

    String generateFighterURL(String urlName, int alt, T artType);

    BufferedImage getFighterImageOnline(Fighter fighter, T artType)
            throws OnlineImageNotFoundException;
}
