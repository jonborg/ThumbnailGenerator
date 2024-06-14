package fighter;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

@Getter
public class FighterImage {
    private final Logger LOGGER = LogManager.getLogger(FighterImage.class);

    private final Fighter fighter;
    private final FighterImageSettings imageSettings;
    private final BufferedImage image;

    public FighterImage(Fighter fighter, FighterImageSettings imageSettings, BufferedImage image){
        this.fighter = fighter;
        this.imageSettings = imageSettings;
        this.image = editImage(image);
    }

    private BufferedImage editImage(BufferedImage bufferedImage){
        bufferedImage = this.resizeImage(bufferedImage, fighter.getUrlName());
        bufferedImage = this.offsetImage(bufferedImage, fighter.getUrlName());
        bufferedImage = this.flipImage(bufferedImage, fighter.isFlip());
        bufferedImage = this.cropImage(bufferedImage, 1280/2,720);
        return bufferedImage;
    }

    private  BufferedImage flipImage(BufferedImage bufferedImage, boolean flip){
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



    private BufferedImage resizeImage(BufferedImage bufferedImage, String urlName) {
        LOGGER.info("Performing resize of image with width {} and height {}.", bufferedImage.getWidth(), bufferedImage.getHeight());
        double scale = this.imageSettings.getScale();
        int width = (int) (scale * bufferedImage.getWidth());
        int height = (int) (scale * bufferedImage.getHeight());

        LOGGER.info("Resize complete to width {} and height {}.", bufferedImage.getWidth(), bufferedImage.getHeight());
        return Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, bufferedImage.getHeight() < bufferedImage.getWidth() ? Scalr.Mode.FIT_TO_HEIGHT : Scalr.Mode.FIT_TO_WIDTH,
                Math.max(width, height), Math.max(width, height), Scalr.OP_ANTIALIAS);
    }




    private BufferedImage offsetImage(BufferedImage bufferedImage, String urlName){
        int offsetX = 2 * this.imageSettings.getOffset()[0];
        int offsetY = this.imageSettings.getOffset()[1];

        BufferedImage img = new BufferedImage(bufferedImage.getWidth() + Math.abs(offsetX),
                                                bufferedImage.getHeight() + Math.abs(offsetY),
                                                    BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        int inputX=0;
        int inputY=0;

        if(offsetX>0) inputX=offsetX;
        if(offsetY>0) {
            inputY=offsetY;
            g2d.drawImage(bufferedImage,inputX,inputY,null);
        }else{
            g2d.drawImage(cropImageY(bufferedImage,-offsetY),inputX,0,null);
        }
        return img;
    }

    private BufferedImage cropImage(BufferedImage bufferedImage, int widthLimit, int heightLimit){
        LOGGER.info("Cropping character image to fit in thumbnail half.");
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int marginX = 0;
        int marginY= 0;

        if (width>widthLimit) {
            marginX = (width-widthLimit)/2;
        }
        LOGGER.info("Character image crop to width {} and height {}.", bufferedImage.getWidth(), bufferedImage.getHeight());
        return Scalr.crop(bufferedImage, marginX,marginY,Math.min(width,widthLimit), Math.min(height,heightLimit), null);
    }

    private BufferedImage cropImageY(BufferedImage img, int y){
        int width = img.getWidth();
        int height = img.getHeight();

        return Scalr.crop(img,0,y,width,height-y,null);
    }

    static public void convertToAlternateRender(Fighter fighter){
        if (fighter.getUrlName() != null){
            switch(fighter.getUrlName().toLowerCase()){
                case "ike":
                case "pokemon_trainer":
                case "villager":
                case "wii_fit_trainer":
                case "robin":
                case "cloud":
                case "corrin":
                case "bayonetta":
                case "inkling":
                case "byleth":
                case "kazuya":
                    if (fighter.getAlt() % 2 == 0) fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "olimar":
                    if (fighter.getAlt() > 4 ) fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "mii_gunner":
                case "random":
                    if (fighter.getAlt() == 2) fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "bowser_jr":
                    fighter.setUrlName(fighter.getUrlName() + fighter.getAlt());
                    break;
                case "joker":
                case "sephiroth":
                    if (fighter.getAlt()<7) fighter.setUrlName(fighter.getUrlName() + "1");
                    else fighter.setUrlName(fighter.getUrlName() + "2");
                    break;
                case "dq_hero":
                case "sora":
                    if (fighter.getAlt() % 4 == 1) fighter.setUrlName(fighter.getUrlName() + "1");
                    if (fighter.getAlt() % 4 == 2) fighter.setUrlName(fighter.getUrlName() + "2");
                    if (fighter.getAlt() % 4 == 3) fighter.setUrlName(fighter.getUrlName() + "3");
                    if (fighter.getAlt() % 4 == 0) fighter.setUrlName(fighter.getUrlName() + "4");
                    break;
                default:
            }
        }
    }
}
