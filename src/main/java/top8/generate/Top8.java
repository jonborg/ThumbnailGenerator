package top8.generate;

import com.google.gson.reflect.TypeToken;
import exception.FighterImageSettingsNotFoundException;
import exception.OnlineImageNotFoundException;
import fighter.DownloadFighterURL;
import fighter.Fighter;
import file.FileUtils;
import file.json.JSONReader;
import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import lombok.var;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnail.generate.Thumbnail;
import top8.image.FighterImage;
import top8.image.ImageSettings;

import static fighter.FighterImage.convertToAlternateRender;

public class Top8 {

    private static final Logger LOGGER = LogManager.getLogger(Top8.class);

    private static String localFightersPath;

    private static Top8Settings ts;

    public static void generateTop8(Top8Settings top8Settings)
            throws IOException, FighterImageSettingsNotFoundException {

        ts = top8Settings;
        localFightersPath = FileUtils.getLocalFightersPath(ts.getArtType());

        var top8 = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        var background = ImageIO.read(new File("assets/tournaments/backgrounds/top8/weeklyl.png"));
        var foreground = ImageIO.read(new File("assets/tournaments/foregrounds/top8/weeklyl.png"));

        Graphics2D g2d = top8.createGraphics();
        //g2d.drawImage(background, 0, 0, null);

        List<PlayerSlot> slots = JSONReader.getJSONArray("settings/top8/slot/weeklyL_player_slots.json",
                new TypeToken<ArrayList<PlayerSlot>>() {}.getType());
        for (int i = 0; i < 8; i++){
           try {
               var place = i + 1;
               var player = top8Settings.getPlayers().get(i);
               var slot = slots.stream().filter(s -> s.getPlace() == place)
                       .findFirst().orElse(null);
               if (slot == null) {
                   return;
               }
               var mask = ImageIO.read(new File(slot.getMask()));
               var fighter = getFighterImage(player.getFighter(0));
               convertToAlternateRender(player.getFighter(0));

               ImageSettings imageSettings = (ImageSettings)
                       JSONReader.getJSONArray(
                               "settings/top8/images/default.json",
                               new TypeToken<ArrayList<ImageSettings>>() {
                               }.getType()).get(0);
               var fighterImageSettings = imageSettings
                       .findFighterImageSettings(
                               player.getFighter(0).getUrlName());
               player.setFighterFlip(0,
                       fighterImageSettings.getSlotImageSettings(place)
                               .isFlip());
               var fighterImage = new FighterImage(player.getFighter(0),
                       fighterImageSettings.getSlotImageSettings(place),
                       fighter);

               BufferedImage maskedImage;
               if (slots.get(i).getShadow() != null) {
                   var shadowSettings = slot.getShadow();
                   var offsetX = shadowSettings.getScaledX(fighterImageSettings
                                   .getSlotImageSettings(place)
                                   .getScale());
                   var offsetY = shadowSettings.getScaledY(fighterImageSettings
                                   .getSlotImageSettings(place)
                                   .getScale());
                   var shadowedImage = addShadow(fighterImage.getImage(),
                           shadowSettings.getColor(), offsetX, offsetY);
                   maskedImage = applyMask(shadowedImage, mask);
               } else {
                   maskedImage = applyMask(fighterImage.getImage(), mask);
               }
               var completeImage = addAdditionalFighters(maskedImage, slot, player.getSecondaryFighters());
               g2d.drawImage(completeImage, slot.getCoordinateY(),
                       slot.getCoordinateX(), null);
           } catch (Exception e){
               LOGGER.error("Error occurred when generating top8.", e);
           }
        }
        //g2d.drawImage(foreground, 0, 0, null);

        var dir = new File("generated_top8/");
        if (!dir.exists()) dir.mkdir();
        var file = new File("generated_top8/a_test.png");
        ImageIO.write(top8, "png", file);

    }

    public static BufferedImage applyMask(BufferedImage image, BufferedImage mask) {
        BufferedImage dest = new BufferedImage(mask.getWidth(), mask.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        AlphaComposite
                ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
        g2.setComposite(ac);
        g2.drawImage(mask, 0, 0, null);
        g2.dispose();
        return dest;
    }

    public static BufferedImage createDropShadow(BufferedImage image,
                                                 int size, float opacity) {

        int width = image.getWidth() + size * 2;
        int height = image.getHeight() + size * 2;

        BufferedImage mask = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = mask.createGraphics();
        g2.drawImage(image, size, size, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN,
                opacity));
        g2.setColor(Color.MAGENTA);
        g2.fillRect(0, 0, width, height);
        g2.dispose();

        BufferedImage shadow = createBlurOp(size).filter(mask, null);
        g2 = shadow.createGraphics();
        g2.dispose();

        return shadow;
    }

    private static ConvolveOp createBlurOp(int size) {
        float[] data = new float[size * size];
        float value = 1f / (float) (size * size);
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
        return new ConvolveOp(new Kernel(size, size, data),
                ConvolveOp.EDGE_NO_OP, null);
    }

    private static BufferedImage addShadow(BufferedImage image, int shadowColor, int offsetX, int offsetY){
        var result = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g2 = result.createGraphics();

        var filter = new ShadowFilter(shadowColor);
        var producer = new FilteredImageSource(image.getSource(), filter);
        var shadow = Toolkit.getDefaultToolkit().createImage(producer);
        g2.drawImage(shadow, offsetX, offsetY, null);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return result;
    }

    static void saveImage(BufferedImage image, File file) {
        try {
            LOGGER.info("Saving thumbnail {} on {}", file.getName(), file.getAbsolutePath());
            ImageIO.write(image, "png", file);
            LOGGER.info("Thumbnail saved successfully.");
        } catch (IOException e) {
            LOGGER.error("Thumbnail could not be saved.");
            LOGGER.catching(e);
        }
    }

    static BufferedImage getFighterImage(Fighter fighter) throws
            OnlineImageNotFoundException {
        if (ts.isLocally()) {
            File directory = new File(localFightersPath);
            String fighterDirPath = localFightersPath + fighter.getUrlName() + "/";
            File fighterDir = new File(fighterDirPath);
            BufferedImage image;

            if (!directory.exists()) directory.mkdir();
            if (!fighterDir.exists()) fighterDir.mkdir();

            File localImage = new File(fighterDirPath + fighter.getAlt()+".png");
            try {
                LOGGER.debug("Trying to find local image for alt {} of {}.", fighter.getAlt(), fighter.getName());
                image = ImageIO.read(localImage);
                return image;
            } catch (IOException e) {
                LOGGER.debug("Image for {} does not exist locally. Will now try finding it online.", fighter.getUrlName());
            }

            //if cannot find locally, will try to find online
            image = DownloadFighterURL.getFighterImageOnline(fighter, ts.getArtType());
            saveImage(image, localImage);
            return image;
        } else {
            return DownloadFighterURL.getFighterImageOnline(fighter, ts.getArtType());
        }
    }

    public static BufferedImage addAdditionalFighters(BufferedImage imageSlot, PlayerSlot playerSlot, List<Fighter> fighters) {
        for (int i = 0; i< fighters.size(); i++){
            var fighter = fighters.get(i);
            var posX = new ExpressionBuilder(playerSlot.getAddFighterPosX())
                    .variable("i")
                    .build()
                    .setVariable("i", i)
                    .evaluate();
            var posY = new ExpressionBuilder(playerSlot.getAddFighterPosY())
                    .variable("i")
                    .build()
                    .setVariable("i", i)
                    .evaluate();
            try {
                var icon = ImageIO.read(Top8.class.getResourceAsStream(
                        "/icons/" + fighter.getUrlName() + "/" + fighter.getAlt() +
                                ".png"));

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
