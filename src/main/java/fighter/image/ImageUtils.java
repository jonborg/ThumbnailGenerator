package fighter.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

public class ImageUtils {
    private static final Logger LOGGER = LogManager.getLogger(ImageUtils.class);

    private final static float WEIGHT_X = 2.0f;
    private final static float WEIGHT_Y = 1.0f;

    public static BufferedImage flipImage(BufferedImage bufferedImage,
                                          boolean flip) {
        if (flip) {
            LOGGER.info("Flipping character image.");
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-bufferedImage.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx,
                    AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            return op.filter(bufferedImage, null);
        }
        return bufferedImage;
    }


    public static BufferedImage resizeImage(BufferedImage bufferedImage,
                                            String urlName, double scale) {
        LOGGER.info("Performing resize of image with width {} and height {}.",
                bufferedImage.getWidth(), bufferedImage.getHeight());
        int width = (int) (scale * bufferedImage.getWidth());
        int height = (int) (scale * bufferedImage.getHeight());

        LOGGER.info("Resize complete to width {} and height {}.",
                bufferedImage.getWidth(), bufferedImage.getHeight());
        return Scalr
                .resize(bufferedImage, Scalr.Method.ULTRA_QUALITY,
                        bufferedImage.getHeight() < bufferedImage.getWidth() ?
                                Scalr.Mode.FIT_TO_HEIGHT :
                                Scalr.Mode.FIT_TO_WIDTH,
                        Math.max(width, height), Math.max(width, height),
                        Scalr.OP_ANTIALIAS);
    }


    public static BufferedImage offsetImage(BufferedImage bufferedImage, int[] offset) {
        int offsetX = (int) Math.floor(WEIGHT_X * offset[0]);
        int offsetY = (int) Math.floor(WEIGHT_Y * offset[1]);

        BufferedImage img =
                new BufferedImage(bufferedImage.getWidth() + Math.abs(offsetX),
                        bufferedImage.getHeight() + Math.abs(offsetY),
                        BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        int inputX = 0;
        int inputY = 0;

        if (offsetX > 0) inputX = offsetX;
        if (offsetY > 0) {
            inputY = offsetY;
            g2d.drawImage(bufferedImage, inputX, inputY, null);
        } else {
            g2d.drawImage(cropImageY(bufferedImage, -offsetY), inputX, 0, null);
        }
        return img;
    }

    public static BufferedImage cropImage(BufferedImage bufferedImage,
                                          int widthLimit, int heightLimit) {
        LOGGER.info("Cropping character image to fit in thumbnail half.");
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int marginX = 0;
        int marginY = 0;

        if (width > widthLimit) {
            marginX = (width - widthLimit) / 2;
        }
        LOGGER.info("Character image crop to width {} and height {}.",
                bufferedImage.getWidth(), bufferedImage.getHeight());
        return Scalr.crop(bufferedImage, marginX, marginY,
                Math.min(width, widthLimit), Math.min(height, heightLimit),
                null);
    }

    public static BufferedImage cropImageY(BufferedImage img, int y) {
        int width = img.getWidth();
        int height = img.getHeight();

        return Scalr.crop(img, 0, y, width, height - y, null);
    }

    public static BufferedImage applyMask(BufferedImage image,
                                          BufferedImage mask, int[] offset) {
        BufferedImage dest =
                new BufferedImage(mask.getWidth(), mask.getHeight(),
                        BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, offset[0], offset[1], null);
        AlphaComposite
                ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
        g2.setComposite(ac);
        g2.drawImage(mask, 0, 0, null);
        g2.dispose();
        return dest;
    }


    public static BufferedImage createShadow(BufferedImage image,
                                             int shadowColor) {
        var result = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g2 = result.createGraphics();

        var filter = new ShadowFilter(shadowColor);
        var producer = new FilteredImageSource(image.getSource(), filter);
        var shadow = Toolkit.getDefaultToolkit().createImage(producer);
        g2.drawImage(shadow, 0, 0, null);
        g2.dispose();

        return result;
    }

    public static BufferedImage mergeShadow(BufferedImage image,
                                            BufferedImage shadow) {
        var result = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g2 = result.createGraphics();
        g2.drawImage(shadow, 0, 0, null);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return result;
    }
}
