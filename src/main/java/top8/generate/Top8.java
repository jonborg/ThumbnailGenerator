package top8.generate;

import com.google.gson.reflect.TypeToken;
import exception.FighterImageSettingsNotFoundException;
import exception.OnlineImageNotFoundException;
import fighter.DownloadFighterURL;
import fighter.Fighter;
import fighter.image.FighterImage;
import fighter.image.ImageUtils;
import file.FileUtils;
import file.json.JSONReader;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.var;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top8.image.FighterImageTop8;
import top8.image.settings.ImageSettings;
import top8.image.slot.PlayerSlot;

public class Top8 {

    private static final Logger LOGGER = LogManager.getLogger(Top8.class);

    private static String localFightersPath;

    private static Top8Settings ts;

    public static void generateTop8(Top8Settings top8Settings)
            throws IOException {

        ts = top8Settings;
        localFightersPath = FileUtils.getLocalFightersPath(ts.getArtType());

        var top8 = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        var background = ImageIO.read(new File("assets/tournaments/backgrounds/top8/weeklyl.png"));
        var foreground = ImageIO.read(new File("assets/tournaments/foregrounds/top8/weeklyl.png"));

        Graphics2D g2d = top8.createGraphics();
        g2d.drawImage(background, 0, 0, null);

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
               var fighter = getFighterImage(player.getFighter(0));
               FighterImage.convertToAlternateRender(player.getFighter(0));

               ImageSettings imageSettings = (ImageSettings)
                       JSONReader.getJSONArray(
                               "settings/top8/images/default.json",
                               new TypeToken<ArrayList<ImageSettings>>() {
                               }.getType()).get(0);
               var fighterImageSettings = imageSettings
                       .findFighterImageSettings(
                               player.getFighter(0).getUrlName());
               player.setFighterFlip(0,
                       fighterImageSettings.getSlotImageTop8Settings(place)
                               .isFlip());

               var fighterImage = new FighterImageTop8(player.getFighterList(),
                       fighterImageSettings.getSlotImageTop8Settings(place), slot,
                       fighter);
               g2d.drawImage(fighterImage.getImage(), slot.getCoordinateY(),
                       slot.getCoordinateX(), null);
           } catch (Exception e){
               LOGGER.error("Error occurred when generating top8.", e);
           }
        }
        g2d.drawImage(foreground, 0, 0, null);

        var dir = new File("generated_top8/");
        if (!dir.exists()) dir.mkdir();
        var file = new File("generated_top8/a_test.png");
        ImageIO.write(top8, "png", file);

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
}
