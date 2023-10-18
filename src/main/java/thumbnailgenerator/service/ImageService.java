package thumbnailgenerator.service;

import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import thumbnailgenerator.exception.LocalImageNotFoundException;

@Service
public class ImageService {

    private static final Logger LOGGER = LogManager.getLogger(ImageService.class);

    public void drawImageFromPathFile(String pathname, Graphics2D g2d) throws
            LocalImageNotFoundException {
        try {
            g2d.drawImage(ImageIO.read(new FileInputStream(pathname)), 0, 0, null);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("An issue occurred when loading image in path {}:", pathname);
            LOGGER.catching(e);
            throw new LocalImageNotFoundException(pathname);
        }
    }

}
