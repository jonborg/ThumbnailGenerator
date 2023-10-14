package top8.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import fighter.Fighter;
import fighter.image.FighterImage;
import fighter.image.ImageUtils;
import fighter.image.settings.FighterImageSettings;
import lombok.Getter;
import lombok.var;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import top8.generate.Top8;
import top8.image.slot.PlayerSlot;
import ui.factory.alert.AlertFactory;

import javax.imageio.ImageIO;

@Getter
public class FighterImageTop8 extends FighterImage {
    private final Logger LOGGER = LogManager.getLogger(FighterImageTop8.class);

    private PlayerSlot playerSlot;
    private List<Fighter> secondaryFighters;

    public FighterImageTop8(List<Fighter> fighters,
                            FighterImageSettings slotImageSettings,
                            PlayerSlot playerSlot,
                            BufferedImage image) {
        super(fighters.get(0), slotImageSettings, image);
        this.playerSlot = playerSlot;
        this.secondaryFighters = fighters.subList(1, fighters.size());
        this.image = editImage(image);
    }

    protected BufferedImage editImage(BufferedImage bufferedImage){
        LOGGER.info(fighterImageSettings.getOffset()[0]+ " "+ fighterImageSettings.getOffset()[1]);
        bufferedImage = ImageUtils.resizeImage(bufferedImage, fighterImageSettings.getScale());

        try {
            var mask = ImageIO.read(new File(playerSlot.getMask()));
            var maskedImage = ImageUtils.applyMask(bufferedImage, mask, fighterImageSettings.getOffset());
            if(playerSlot.getShadow()!=null) {
                var shadowSettings = playerSlot.getShadow();
                var offsetX = fighter.isFlip() ?
                        fighterImageSettings.getOffset()[0]
                                - shadowSettings.getCoordinateX() :
                        fighterImageSettings.getOffset()[0]
                                + shadowSettings.getCoordinateX();
                var offsetY = fighterImageSettings.getOffset()[1]
                        + shadowSettings.getCoordinateY();
                var offset = new int[] {
                        offsetX, offsetY};
                var shadowImage = ImageUtils
                        .createShadow(bufferedImage, shadowSettings.getColor());
                shadowImage = ImageUtils.applyMask(shadowImage, mask, offset);
                bufferedImage = ImageUtils.mergeShadow(maskedImage, shadowImage);
            }
        } catch (IOException e){
            LOGGER.error("Could not find image mask in directory " + playerSlot.getMask());
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
        bufferedImage = ImageUtils.flipImage(bufferedImage, fighter.isFlip());
        bufferedImage = addAdditionalFighters(bufferedImage, playerSlot, secondaryFighters);
        return bufferedImage;
    }

    public BufferedImage addAdditionalFighters(BufferedImage imageSlot, PlayerSlot playerSlot, List<Fighter> fighters) {
        for (int i = 0; i< fighters.size(); i++){
            var fighter = fighters.get(i);
            var scale = playerSlot.getAdditionalFighters().getScale();
            var posX = new ExpressionBuilder(playerSlot.getAdditionalFighters().getCoordinateX())
                    .variable("i")
                    .build()
                    .setVariable("i", i)
                    .evaluate();
            var posY = new ExpressionBuilder(playerSlot.getAdditionalFighters().getCoordinateY())
                    .variable("i")
                    .build()
                    .setVariable("i", i)
                    .evaluate();
            try {
                var icon = ImageIO.read(Top8.class.getResourceAsStream(
                        "/icons/" + fighter.getUrlName() + "/" + fighter.getAlt() +
                                ".png"));
                icon = ImageUtils.resizeImage(icon, scale);
                var g2d = imageSlot.createGraphics();
                g2d.drawImage(icon, (int) posX, (int) posY, null);
                g2d.dispose();
            } catch (IOException e){
                LOGGER.error("Error occurred when adding additional fighters.", e);
            }
        }
        return imageSlot;
    }
}

