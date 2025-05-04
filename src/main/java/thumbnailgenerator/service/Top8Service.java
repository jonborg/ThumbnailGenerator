package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import exception.Top8FromFileException;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.dto.PlayerSlot;
import thumbnailgenerator.dto.SlotImageTop8Settings;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.dto.Top8ImageSettings;
import thumbnailgenerator.dto.FullSlot;
import thumbnailgenerator.dto.Top8;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.image.ImageUtils;
import thumbnailgenerator.service.json.JSONReaderService;

@Service
public class Top8Service {

    private static final Logger LOGGER = LogManager.getLogger(Top8Service.class);
    private static Top8 top8;
    private @Autowired GameEnumService gameEnumService;
    private @Autowired ImageService imageService;
    private @Autowired Top8FileService top8FileService;
    private @Autowired SmashUltimateCharacterService smashUltimateCharacterService;
    private @Autowired JSONReaderService jsonReaderService;
    private @Value("${top8.path.save}") String saveTop8Path;

    public void generateTop8FromFile(InputStream inputStream, Boolean saveLocally)
            throws Top8FromFileException {
        var top8 = top8FileService.getTop8FromFile(inputStream,saveLocally);
        var invalidThumbnailList = new ArrayList<Thumbnail>();
        try {
            generateTop8(top8);
        }catch(IOException e) {
            AlertFactory.displayError(e.getMessage());
            throw new Top8FromFileException();
        }
    }

    public void generateTop8(Top8 top8)
            throws IOException {

        var fullSlot = (FullSlot) jsonReaderService.getJSONObjectFromFile(
                top8.getFileTop8Settings().getSlotSettingsFile(),
                new TypeToken<FullSlot>() {}.getType());

        var result = new BufferedImage(fullSlot.getWidth(), fullSlot.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        LOGGER.info("Drawing background in path {}.", top8.getFileTop8Settings().getBackground());
        imageService.drawImageFromPathFile(top8.getFileTop8Settings().getBackground(), g2d);

        this.drawCharacters(top8, fullSlot, g2d);

        LOGGER.info("Drawing foreground in path {}.", top8.getFileTop8Settings().getForeground());
        imageService.drawImageFromPathFile(top8.getFileTop8Settings().getForeground(), g2d);

        var dir = new File(saveTop8Path);
        if (!dir.exists()) dir.mkdir();
        var file = new File(saveTop8Path + top8.getPlayers().get(0).getFighter(0).getUrlName() + ".png");
        ImageIO.write(result, "png", file);

    }

    private void drawCharacters(Top8 top8, FullSlot fullSlot, Graphics2D g2d){
        var characterImageFetcher = gameEnumService.getCharacterImageFetcher(top8.getGame());
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
                smashUltimateCharacterService.convertToAlternateRender(player.getFighter(0));

                Top8ImageSettings top8ImageSettings = (Top8ImageSettings)
                        jsonReaderService.getJSONArrayFromFile(top8.getFileTop8Settings()
                                        .getFighterImageSettingsFile(top8.getArtType()),
                                new TypeToken<ArrayList<Top8ImageSettings>>() {}.getType()).get(0);

                var fighterImageTop8Settings = top8ImageSettings
                        .findFighterImageSettings(
                                player.getFighter(0).getUrlName());
                player.setFighterFlip(0,
                        fighterImageTop8Settings.getSlotImageTop8Settings(place)
                                .isFlip());

                characterImage = editCharacterImage(characterImage, player, slot, top8.getGame(),
                        fighterImageTop8Settings.getSlotImageTop8Settings(place));

                g2d.drawImage(characterImage, slot.getCoordinateY(),
                        slot.getCoordinateX(), null);
            } catch (Exception e){
                LOGGER.error("Error occurred when generating top8.", e);
            }
        }
    }

    public BufferedImage editCharacterImage(BufferedImage characterImage, Player player,
                                            PlayerSlot playerSlot, Game game, SlotImageTop8Settings slotImageTop8Settings) {

        LOGGER.info(slotImageTop8Settings.getOffset()[0]+ " "+ slotImageTop8Settings.getOffset()[1]);
        characterImage = imageService.resizeImage(characterImage, slotImageTop8Settings.getScale());

        try {
            var mask = ImageIO.read(new File(playerSlot.getMask()));
            var maskedImage = imageService.applyMask(characterImage, mask, slotImageTop8Settings.getOffset());
            if(playerSlot.getShadow()!=null) {
                var shadowSettings = playerSlot.getShadow();
                var offsetX = player.getFighter(0).isFlip() ?
                        slotImageTop8Settings.getOffset()[0] - shadowSettings.getCoordinateX() :
                        slotImageTop8Settings.getOffset()[0] + shadowSettings.getCoordinateX();
                var offsetY = slotImageTop8Settings.getOffset()[1] + shadowSettings.getCoordinateY();
                var offset = new int[] {offsetX, offsetY};
                var shadowImage = imageService.createShadow(characterImage, shadowSettings.getColor());
                shadowImage = imageService.applyMask(shadowImage, mask, offset);
                characterImage = imageService.mergeShadow(maskedImage, shadowImage);
            }
        } catch (IOException e){
            LOGGER.error("Could not find image mask in directory " + playerSlot.getMask());
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
        characterImage = imageService.flipImage(characterImage, player.getFighter(0).isFlip());
        characterImage = addAdditionalFighters(characterImage, playerSlot,
                player.getSecondaryFighters(), game);
        return characterImage;
    }

    public BufferedImage addAdditionalFighters(BufferedImage characterImage, PlayerSlot playerSlot, List<Fighter> fighters, Game game) {
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
                var icon = ImageIO.read(Top8Service.class.getResourceAsStream(
                        "/icons/" + game.getCode() + "/" + fighter.getUrlName() + "/" + fighter.getAlt() +
                                ".png"));
                icon = ImageUtils.resizeImage(icon, scale);
                var g2d = characterImage.createGraphics();
                g2d.drawImage(icon, (int) posX, (int) posY, null);
                g2d.dispose();
            } catch (IOException e){
                LOGGER.error("Error occurred when adding additional fighters.", e);
            }
        }
        return characterImage;
    }
}
