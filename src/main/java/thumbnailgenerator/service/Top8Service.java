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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.FighterImage;
import thumbnailgenerator.dto.Top8ImageSettings;
import thumbnailgenerator.dto.FullSlot;
import thumbnailgenerator.dto.Top8;
import thumbnailgenerator.factory.CharacterImageFetcherFactory;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.file.FileUtils;
import thumbnailgenerator.utils.json.JSONReader;

@Service
public class Top8Service {

    private static final Logger LOGGER = LogManager.getLogger(Top8Service.class);
    private static String localFightersPath;
    private static Top8 ts;
    private @Autowired CharacterImageFetcherFactory characterImageFetcherFactory;

    public void generateTop8(Top8 top8Settings)
            throws IOException {

        ts = top8Settings;
        localFightersPath = FileUtils.getLocalFightersPath(top8Settings);

        var fullSlot = (FullSlot) JSONReader.getJSONObjectFromFile(ts.getTournament()
                        .getTop8Settings().getSlotSettingsFile(),
                new TypeToken<FullSlot>() {}.getType());

        var top8 = new BufferedImage(fullSlot.getWidth(), fullSlot.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var characterImageFetcher = characterImageFetcherFactory.getCharacterImageFetcher(ts.getGame());
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
               BufferedImage characterImage = characterImageFetcher
                       .getCharacterImage(player.getFighter(0), top8Settings);
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

               FighterImageTop8 fighterImage = new FighterImageTop8(player.getFighterList(),
                       fighterImageSettings.getSlotImageTop8Settings(place), slot,
                       characterImage);
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
}
