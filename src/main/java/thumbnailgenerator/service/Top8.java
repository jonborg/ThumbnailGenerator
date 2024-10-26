package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.FighterImage;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Top8ImageSettings;
import thumbnailgenerator.dto.FullSlot;
import thumbnailgenerator.dto.Top8Settings;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.file.FileUtils;
import thumbnailgenerator.utils.json.JSONReader;

public class Top8 {

    private static final Logger LOGGER = LogManager.getLogger(Top8.class);

    private static String localFightersPath;

    private static Top8Settings ts;

    public static void generateTop8(Top8Settings top8Settings)
            throws IOException {

        ts = top8Settings;
        localFightersPath = FileUtils.getLocalFightersPath(ts.getArtType());

        var fullSlot = (FullSlot) JSONReader.getJSONObjectFromFile(ts.getTournament()
                        .getTop8Settings().getSlotSettingsFile(),
                new TypeToken<FullSlot>() {}.getType());

        var top8 = new BufferedImage(fullSlot.getWidth(), fullSlot.getHeight(), BufferedImage.TYPE_INT_ARGB);

        BufferedImage background = null;
        BufferedImage foreground = null;
        if(ts.getTournament().getTop8Settings().getBackground() != null &&
                !ts.getTournament().getTop8Settings().getBackground().isEmpty()) {
            try {
                background = ImageIO.read(new File(
                        ts.getTournament().getTop8Settings().getBackground()));
            } catch (Exception e) {
                LOGGER.warn("Could not load background image for top8.", e);
                AlertFactory.displayWarning("Could not load background image for top8.");
            }
        }
        if(ts.getTournament().getTop8Settings().getForeground() != null &&
                !ts.getTournament().getTop8Settings().getForeground().isEmpty()) {
            try {
                foreground = ImageIO.read(new File(
                        ts.getTournament().getTop8Settings().getForeground()));
            } catch (Exception e) {
                LOGGER.warn("Could not load foreground image for top8.", e);
                AlertFactory.displayWarning("Could not load foreground image for top8.");
            }
        }

        Graphics2D g2d = top8.createGraphics();
        g2d.drawImage(background, 0, 0, null);

        var slots = fullSlot.getSlots();
        for (int i = 0; i < slots.size(); i++){
           try {
               var place = i + 1;
               var player = top8Settings.getPlayers().get(i);
               var slot = slots.stream().filter(s -> s.getPlace() == place)
                       .findFirst().orElse(null);
               if (slot == null) {
                   return;
               }
               var fighter = getFighterImage(player.getFighter(0), top8Settings.getGame());
               FighterImage.convertToAlternateRender(player.getFighter(0));

               Top8ImageSettings top8ImageSettings = (Top8ImageSettings)
                       JSONReader.getJSONArrayFromFile(
                               ts.getTournament().getTop8Settings()
                                       .getFighterImageSettingsFile(ts.getArtType()),
                               new TypeToken<ArrayList<Top8ImageSettings>>() {
                               }.getType()).get(0);
               var fighterImageSettings = top8ImageSettings
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
        var file = new File("generated_top8/top8_"+ Instant.now().toEpochMilli() +".png");
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

    static BufferedImage getFighterImage(Fighter fighter, Game game) throws
            OnlineImageNotFoundException {
        DownloadFighterURL downloadFighterURL;
        if (Game.SF6.equals(game)){
            downloadFighterURL = new StreetFighter6DownloadFighterURL();
        } else {
            downloadFighterURL = new SmashUltimateDownloadFighterURL();
        }
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
            image = downloadFighterURL.getFighterImageOnline(fighter, ts.getArtType());
            saveImage(image, localImage);
            return image;
        } else {
            return downloadFighterURL.getFighterImageOnline(fighter, ts.getArtType());
        }
    }
}
