package thumbnail.image;

import fighter.Fighter;
import fighter.image.FighterImage;
import fighter.image.ImageUtils;
import thumbnail.image.settings.FighterImageThumbnailSettings;

import java.awt.image.BufferedImage;

public class FighterImageThumbnail extends FighterImage {

    private final int THUMBNAIL_WIDTH = 1280;
    private final int THUMBNAIL_HEIGHT = 720;

    public FighterImageThumbnail(Fighter fighter,
                                 FighterImageThumbnailSettings imageSettings,
                                 BufferedImage image) {
        super(fighter, imageSettings, image);
        this.image = editImage(image);
    }

    @Override
    protected BufferedImage editImage(BufferedImage bufferedImage) {
        bufferedImage = ImageUtils.resizeImage(bufferedImage, fighterImageSettings.getScale());
        bufferedImage = ImageUtils.offsetImage(bufferedImage, fighterImageSettings.getOffset());
        bufferedImage = ImageUtils.flipImage(bufferedImage, fighter.isFlip());
        bufferedImage = ImageUtils.cropImage(bufferedImage, THUMBNAIL_WIDTH/2,THUMBNAIL_HEIGHT, fighterImageSettings.getOffset());
        return bufferedImage;
    }
}
