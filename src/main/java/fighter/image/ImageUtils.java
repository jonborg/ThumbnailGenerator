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
                                            double scale) {
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
        int offsetImageWidth = Math.max(bufferedImage.getWidth(), bufferedImage.getWidth() + offset[0]);
        int offsetImageHeight = Math.max(bufferedImage.getHeight(), bufferedImage.getHeight() + offset[1]);

        int offsetOriginX= Math.max(0, offset[0]);
        int offsetOriginY= Math.max(0, offset[1]);

        BufferedImage img = new BufferedImage(offsetImageWidth, offsetImageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(bufferedImage, offsetOriginX, offsetOriginY, null);
        return img;
    }

    public static BufferedImage cropImage(BufferedImage bufferedImage,
                                   int widthLimit, int heightLimit, int[] offsets) {
        LOGGER.info("Cropping character image to fit in thumbnail half.");
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int cropOriginX;
        int cropOriginY;
        int cropWidth;
        int cropHeight;

        if (offsets[0] >0){
            cropOriginX = 0;
            cropWidth = Math.min(width, widthLimit);
        } else {
            cropOriginX = - offsets[0];
            cropWidth = Math.max(Math.min(width - cropOriginX , widthLimit), 0);
        }

        if (offsets[1] >0){
            cropOriginY = 0;
            cropHeight = Math.min(height, heightLimit);
        } else {
            cropOriginY = - offsets[1];
            cropHeight = Math.max(Math.min(height - cropOriginY , heightLimit), 0);
        }

        LOGGER.info("Character image crop to width {} and height {}.",
                bufferedImage.getWidth(), bufferedImage.getHeight());
        if(cropOriginX >= width || cropOriginY >= height){
            return new BufferedImage(widthLimit, heightLimit, BufferedImage.TYPE_INT_ARGB);
        } else {
            return Scalr
                    .crop(bufferedImage, cropOriginX, cropOriginY, cropWidth,
                            cropHeight, null);
        }
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
