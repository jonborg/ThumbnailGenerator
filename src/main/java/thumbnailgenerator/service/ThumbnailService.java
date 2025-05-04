package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.FighterImageThumbnailSettings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.interfaces.FighterArtType;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.exception.FontNotFoundException;
import thumbnailgenerator.exception.LocalImageNotFoundException;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.exception.ThumbnailFromFileException;
import thumbnailgenerator.factory.CharacterImageFetcherFactory;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.service.json.JSONReaderService;

@Service
public class ThumbnailService {

    private static final Logger LOGGER = LogManager.getLogger(ThumbnailService.class);
    private @Autowired CharacterImageFetcherFactory characterImageFetcherFactory;
    private @Autowired TextService textService;
    private @Autowired ImageService imageService;
    private @Autowired ThumbnailFileService thumbnailFileService;
    private @Autowired SmashUltimateCharacterService smashUltimateCharacterService;
    private @Autowired JSONReaderService jsonReaderService;
    private @Value("${thumbnail.size.width}") Integer thumbnailWidth;
    private @Value("${thumbnail.size.height}") Integer thumbnailHeight;
    private @Value("${thumbnail.path.save}") String saveThumbnailsPath;

    public void generateAndSaveThumbnail(Thumbnail thumbnail)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException,
            MalformedURLException {

        var imageResult = generateThumbnail(thumbnail);
        saveGeneratedGraphic(thumbnail, imageResult);
    }

    public void generateFromSmashGG(String text, boolean saveLocally)
            throws FighterImageSettingsNotFoundException, FileNotFoundException,
            ThumbnailFromFileException, FontNotFoundException,
            UnsupportedEncodingException {
        InputStream inputStream = new ByteArrayInputStream(text.getBytes("UTF-8"));
        generateAndSaveThumbnailsFromFile(inputStream, saveLocally);
    }

    public void generateAndSaveThumbnailsFromFile(InputStream inputStream, Boolean saveLocally)
            throws FighterImageSettingsNotFoundException,
            ThumbnailFromFileException, FontNotFoundException {
        var thumbnailList = thumbnailFileService.getListThumbnailsFromFile(inputStream,saveLocally);
        var invalidThumbnailList = new ArrayList<Pair<Thumbnail, String>>();
        for (Thumbnail thumbnail : thumbnailList){
            try {
                generateAndSaveThumbnail(thumbnail);
            }catch(LocalImageNotFoundException e) {
                AlertFactory.displayError(e.getMessage());
                throw new ThumbnailFromFileException();
            }catch(FontNotFoundException e){
                throw e;
            }catch (Exception e){
                invalidThumbnailList.add(new Pair(thumbnail, e.getMessage()));
            }
        }
        if (!invalidThumbnailList.isEmpty()){
            String details = "";
            LOGGER.error("Thumbnails could not be generated from these lines:");
            for (Pair pair :invalidThumbnailList){
                Thumbnail thumbnail = (Thumbnail) pair.getValue0();
                String errorMessage = (String) pair.getValue1();
                LOGGER.error(thumbnail);
                var listPlayers = thumbnail.getPlayers();
                details += listPlayers.get(0).getPlayerName() + " "
                        + listPlayers.get(1).getPlayerName() + " "
                        + thumbnail.getRound() + " -> "
                        + errorMessage + System.lineSeparator();
            }
            AlertFactory.displayError("Thumbnails could not be generated from these lines: ", details);
            throw new ThumbnailFromFileException();
        }
    }

    private BufferedImage generateThumbnail(Thumbnail thumbnail)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException,
            MalformedURLException {
        LOGGER.debug("*********************************************************************************************");
        LOGGER.debug("Creating thumbnail with following parameters:");
        LOGGER.debug("Tournament -> {}", thumbnail.getTournament().getName());
        LOGGER.debug("Player 1 -> {}", thumbnail.getPlayers().get(0).toString());
        LOGGER.debug("Player 2 -> {}", thumbnail.getPlayers().get(1).toString());
        LOGGER.debug("Round -> {}", thumbnail.getRound());
        LOGGER.debug("Date -> {}", thumbnail.getDate());
        LOGGER.debug("Art used -> {}", thumbnail.getArtType());
        LOGGER.debug("*********************************************************************************************");

        var result = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_ARGB);
        var thumbnailSettings = thumbnail.getFileThumbnailSettings();
        var g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        LOGGER.info("Drawing background in path {}.", thumbnail.getFileThumbnailSettings().getBackground());
        imageService.drawImageFromPathFile(thumbnail.getFileThumbnailSettings().getBackground(), g2d);

        this.drawCharacters(thumbnail, g2d);

        LOGGER.info("Drawing foreground in path {}.", thumbnail.getFileThumbnailSettings().getForeground());
        imageService.drawImageFromPathFile(thumbnail.getFileThumbnailSettings().getForeground(), g2d);

        LOGGER.info("Drawing thumbnail text");
        LOGGER.debug("Loading {} text settings: {}", thumbnail.getTournament().getName(),
                thumbnail.getFileThumbnailSettings().getTextSettings());
        this.drawText(thumbnail, g2d);

        return result;
    }

    private void saveGeneratedGraphic(Thumbnail thumbnail, BufferedImage image){
        String thumbnailFileName = thumbnail.getPlayers().get(0).getPlayerName().replace("|","_").replace("/","_")+"-"+
                thumbnail.getPlayers().get(0).getFighter(0).getUrlName()+thumbnail.getPlayers().get(0).getFighter(0).getAlt()+"--"+
                thumbnail.getPlayers().get(1).getPlayerName().replace("|","_").replace("/","_")+"-"+
                thumbnail.getPlayers().get(1).getFighter(0).getUrlName()+thumbnail.getPlayers().get(1).getFighter(0).getAlt()+"--"+
                thumbnail.getRound()+"-"+thumbnail.getDate().replace("/","_").replace(":","_")+".png";

        File dirThumbnails = new File(saveThumbnailsPath);
        if (!dirThumbnails.exists()) dirThumbnails.mkdir();
        var imageFile = new File(saveThumbnailsPath +
                thumbnailFileName);
        try {
            LOGGER.info("Saving thumbnail {} on {}", imageFile.getName(), imageFile.getAbsolutePath());
            ImageIO.write(image, "png", imageFile);
            LOGGER.info("Thumbnail saved successfully.");
        } catch (IOException e) {
            LOGGER.error("Thumbnail could not be saved.");
            LOGGER.catching(e);
        }
    }

    public BufferedImage generatePreview(Tournament tournament, Game game, FighterArtType artType)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException,
            MalformedURLException {
        LOGGER.info("Generating thumbnail preview.");
        List<Player> players = Player.generatePreviewPlayers(game);
        ImageSettings imageSettings = (ImageSettings)
                jsonReaderService.getJSONArrayFromFile(
                        tournament
                                .getThumbnailSettingsByGame(game)
                                .getFighterImageSettingsFile(artType),
                        new TypeToken<ArrayList<ImageSettings>>() {}.getType()).get(0);
        return generateThumbnail(Thumbnail.builder()
                .tournament(tournament)
                .game(game)
                .imageSettings(imageSettings)
                .locally(false)
                .round("Pools Round 1")
                .date("07/12/2018")
                .players(players)
                .artType(artType)
                .build());
    }

    private void drawCharacters(Thumbnail thumbnail, Graphics2D g2d)
            throws MalformedURLException, OnlineImageNotFoundException,
            FighterImageSettingsNotFoundException {
        var port = 0;
        var characterImageFetcher = characterImageFetcherFactory
                .getCharacterImageFetcher(thumbnail.getGame());

        for (Player player : thumbnail.getPlayers()) {
            port++;
            LOGGER.info("Drawing player {} information.", port);
            var fighter = player.getFighter(0);
            var characterImage = characterImageFetcher.getCharacterImage(fighter, thumbnail);
            if (Game.SSBU.equals(thumbnail.getGame())) {
                smashUltimateCharacterService.convertToAlternateRender(fighter);
            }
            var fighterImageThumbnailSettings = thumbnail.getImageSettings()
                    .findFighterImageSettings(fighter.getUrlName());

            characterImage = editCharacterImage(characterImage, fighterImageThumbnailSettings, player);

            LOGGER.info("Drawing player {}'s character: {}", port, fighter.getName());
            if (characterImage.getWidth() < thumbnailWidth / 2 && fighter.isFlip()) {
                g2d.drawImage(characterImage, null, thumbnailWidth / 2 * port - characterImage.getWidth(), 0);
            } else {
                g2d.drawImage(characterImage, null, thumbnailWidth / 2 * (port - 1), 0);
            }
        }
    }

    public BufferedImage editCharacterImage(BufferedImage characterImage , FighterImageThumbnailSettings fighterImageThumbnailSettings, Player player) {
        var isFlip = player.getFighter(0).isFlip() !=
                fighterImageThumbnailSettings.isFlip();
        characterImage = imageService.resizeImage(characterImage, fighterImageThumbnailSettings.getScale());
        characterImage = imageService.offsetImage(characterImage, fighterImageThumbnailSettings.getOffset());
        characterImage = imageService.flipImage(characterImage, isFlip);
        characterImage = imageService.cropImage(characterImage, thumbnailWidth/2, thumbnailHeight);
        return characterImage;
    }

    private void drawText(Thumbnail thumbnail, Graphics2D g2d)
            throws FontNotFoundException {
        g2d.drawImage(textService.convert(thumbnail.getPlayers().get(0).getPlayerName(),
                thumbnail.getFileThumbnailSettings().getTextSettings(), true),
                0, thumbnail.getFileThumbnailSettings().getTextSettings().getDownOffsetTop()[0], null);
        g2d.drawImage(textService.convert(thumbnail.getPlayers().get(1).getPlayerName(),
                thumbnail.getFileThumbnailSettings().getTextSettings(), true),
                thumbnailWidth / 2,  thumbnail.getFileThumbnailSettings().getTextSettings().getDownOffsetTop()[1], null);

        g2d.drawImage(textService.convert(thumbnail.getRound(), thumbnail.getFileThumbnailSettings().getTextSettings(), false),
                0, thumbnailHeight - 100 + thumbnail.getFileThumbnailSettings().getTextSettings().getDownOffsetBottom()[0], null);
        g2d.drawImage(textService.convert(thumbnail.getDate(), thumbnail.getFileThumbnailSettings().getTextSettings(), false),
                thumbnailWidth / 2, thumbnailHeight - 100 + thumbnail.getFileThumbnailSettings().getTextSettings().getDownOffsetBottom()[1], null);
    }
}

