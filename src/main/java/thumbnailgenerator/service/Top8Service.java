package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.FighterImage;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.dto.Top8ImageSettings;
import thumbnailgenerator.dto.FullSlot;
import thumbnailgenerator.dto.Top8;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.exception.FontNotFoundException;
import thumbnailgenerator.exception.ThumbnailFromFileException;
import thumbnailgenerator.factory.CharacterImageFetcherFactory;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.json.JSONReader;

@Service
public class Top8Service {

    private static final Logger LOGGER = LogManager.getLogger(Top8Service.class);
    private static Top8 top8;
    private @Autowired CharacterImageFetcherFactory characterImageFetcherFactory;
    private @Autowired ImageService imageService;
    private @Autowired Top8FileService top8FileService;

    public void generateTop8FromFile(InputStream inputStream, Boolean saveLocally)
            throws FighterImageSettingsNotFoundException,
            ThumbnailFromFileException, FontNotFoundException {
        var top8 = top8FileService.getTop8FromFile(inputStream,saveLocally);
        var invalidThumbnailList = new ArrayList<Thumbnail>();
        try {
            generateTop8(top8);
        }catch(IOException e) {
            AlertFactory.displayError(e.getMessage());
            throw new ThumbnailFromFileException();
        }
    }

    public void generateTop8(Top8 top8)
            throws IOException {

        var fullSlot = (FullSlot) JSONReader.getJSONObjectFromFile(
                top8.getTournament()
                        .getTop8Settings().getSlotSettingsFile(),
                new TypeToken<FullSlot>() {}.getType());

        var result = new BufferedImage(fullSlot.getWidth(), fullSlot.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        LOGGER.info("Drawing background in path {}.", top8.getTournament().getTop8Settings().getBackground());
        imageService.drawImageFromPathFile(top8.getTournament().getTop8Settings().getBackground(), g2d);

        this.drawCharacters(top8, fullSlot, g2d);

        LOGGER.info("Drawing foreground in path {}.", top8.getTournament().getTop8Settings().getForeground());
        imageService.drawImageFromPathFile(top8.getTournament().getTop8Settings().getForeground(), g2d);

        var dir = new File("generated_top8/");
        if (!dir.exists()) dir.mkdir();
        var file = new File("generated_top8/a_test.png");
        ImageIO.write(result, "png", file);

    }

    private void drawCharacters(Top8 top8, FullSlot fullSlot, Graphics2D g2d){
        var characterImageFetcher = characterImageFetcherFactory.getCharacterImageFetcher(top8.getGame());
        var slots = fullSlot.getSlots();

        for (int i = 0; i < 8; i++){
            try {
                var place = i + 1;
                var player = top8.getPlayers().get(i);
                var slot = slots.stream().filter(s -> s.getPlace() == place)
                        .findFirst().orElse(null);
                if (slot == null) {
                    return;
                }
                BufferedImage characterImage = characterImageFetcher
                        .getCharacterImage(player.getFighter(0), top8);
                FighterImage.convertToAlternateRender(player.getFighter(0));

                Top8ImageSettings top8ImageSettings = (Top8ImageSettings)
                        JSONReader.getJSONArrayFromFile(top8.getTournament().getTop8Settings()
                                        .getFighterImageSettingsFile(top8.getArtType()),
                                new TypeToken<ArrayList<Top8ImageSettings>>() {}.getType()).get(0);

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
    }
}
