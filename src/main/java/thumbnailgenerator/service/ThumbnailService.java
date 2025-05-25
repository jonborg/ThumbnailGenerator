package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.FighterImageThumbnailSettings;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.exception.FontNotFoundException;
import thumbnailgenerator.exception.LocalImageNotFoundException;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.exception.ThumbnailFromFileException;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.service.json.JSONReaderService;
import thumbnailgenerator.ui.loading.LoadingState;

@Service
public class ThumbnailService {

    private static final Logger LOGGER = LogManager.getLogger(ThumbnailService.class);
    private @Autowired TextService textService;
    private @Autowired ImageService imageService;
    private @Autowired TournamentService tournamentService;
    private @Autowired ThumbnailFileService thumbnailFileService;
    private @Autowired SmashUltimateCharacterService smashUltimateCharacterService;
    private @Autowired JSONReaderService jsonReaderService;
    private @Autowired GameEnumService gameEnumService;
    private @Autowired ExecutorService executorService;

    private @Value("${thumbnail.size.width}") Integer thumbnailWidth;
    private @Value("${thumbnail.size.height}") Integer thumbnailHeight;
    private @Value("${thumbnail.path.save}") String saveThumbnailsPath;

    private static FileThumbnailSettings fileThumbnailSettings;

    public void generateAndSaveThumbnail(Thumbnail thumbnail)
            throws FighterImageSettingsNotFoundException,
            OnlineImageNotFoundException, MalformedURLException,
            FontNotFoundException, LocalImageNotFoundException {
        var imageResult = generateThumbnail(thumbnail);
        saveGeneratedGraphic(thumbnail, imageResult);
    }

    public void generateFromSmashGG(String text, boolean saveLocally, LoadingState loadingState)
            throws FighterImageSettingsNotFoundException {
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        generateAndSaveThumbnailsFromFile(inputStream, saveLocally, loadingState);
    }

    public void generateAndSaveThumbnailsFromFile(InputStream inputStream, Boolean saveLocally, LoadingState loadingState)
            throws FighterImageSettingsNotFoundException {
        var thumbnailList = thumbnailFileService.getListThumbnailsFromFile(inputStream,saveLocally);
        var invalidThumbnailList = new ArrayList<Pair<Thumbnail, String>>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        AtomicInteger generatedThumbnails = new AtomicInteger();
        for (Thumbnail thumbnail : thumbnailList) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    loadingState.update(true, true,
                            generatedThumbnails.incrementAndGet(), thumbnailList.size());
                    generateAndSaveThumbnail(thumbnail);
                } catch (Exception e) {
                    synchronized (invalidThumbnailList) {
                        invalidThumbnailList.add(new Pair<>(thumbnail, e.getMessage()));
                    }
                }
            }, executorService);
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.thenRun(() -> {
            loadingState.disableLoading();
            if (invalidThumbnailList.isEmpty()) {
                Platform.runLater(() ->
                        AlertFactory.displayInfo("Thumbnails were successfully generated and saved!")
                );
            } else {
                StringBuilder detailsBuilder = new StringBuilder();
                LOGGER.error("Thumbnails could not be generated from these lines:");
                for (Pair<Thumbnail, String> pair : invalidThumbnailList) {
                    Thumbnail thumbnail = pair.getValue0();
                    String errorMessage = pair.getValue1();
                    LOGGER.error("Thumbnail: {}", thumbnail);
                    var listPlayers = thumbnail.getPlayers();
                    detailsBuilder.append(listPlayers.get(0).getPlayerName())
                            .append(" ")
                            .append(listPlayers.get(1).getPlayerName())
                            .append(" ")
                            .append(thumbnail.getRound())
                            .append(" -> ")
                            .append(errorMessage)
                            .append(System.lineSeparator());
                }
                final String details = detailsBuilder.toString();
                Platform.runLater(() ->
                    AlertFactory.displayError("Thumbnails could not be generated from these lines: ", details)
                );
                throw new RuntimeException(new ThumbnailFromFileException());
            }
        }).exceptionally(ex -> {
            // Handle any unexpected exception from the entire batch
            LOGGER.error("Error during batch processing", ex);
            return null;
        });
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
        fileThumbnailSettings = tournamentService.getTournamentThumbnailSettingsOrDefault(thumbnail.getTournament(), thumbnail.getGame());
        var g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        LOGGER.info("Drawing background in path {}.", fileThumbnailSettings.getBackground());
        imageService.drawImageFromPathFile(fileThumbnailSettings.getBackground(), g2d);

        this.drawCharacters(thumbnail, g2d);

        LOGGER.info("Drawing foreground in path {}.", fileThumbnailSettings.getForeground());
        imageService.drawImageFromPathFile(fileThumbnailSettings.getForeground(), g2d);

        LOGGER.info("Drawing thumbnail text");
        LOGGER.debug("Loading {} text settings: {}", thumbnail.getTournament().getName(),
                fileThumbnailSettings.getTextSettings());
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

    public BufferedImage generatePreview(Tournament tournament, Game game, FighterArtTypeEnum artType)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException,
            MalformedURLException {
        LOGGER.info("Generating thumbnail preview.");
        List<Player> players = Player.generatePreviewPlayers(gameEnumService.getAllCharacters(game));
        ImageSettings imageSettings = (ImageSettings)
                jsonReaderService.getJSONObjectFromFile(
                        tournament
                                .getThumbnailSettingsByGame(game)
                                .getFighterImageSettingsFile(artType),
                        new TypeToken<ImageSettings>() {}.getType());
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
        var characterImageFetcher = gameEnumService.getCharacterImageFetcher(thumbnail.getGame());

        for (Player player : thumbnail.getPlayers()) {
            port++;
            LOGGER.info("Drawing player {} information.", port);
            var slot = player.getFighterList().size() > 1
                    ? getCharacterPairSplit(thumbnail, player, characterImageFetcher, port)
                    : getCharacterSingle(thumbnail, player, characterImageFetcher, port);
            g2d.drawImage(slot, null, thumbnailWidth / 2 * (port - 1), 0);
        }
    }

    private BufferedImage getCharacterSingle(Thumbnail thumbnail, Player player, CharacterImageFetcher characterImageFetcher, int port)
            throws MalformedURLException, OnlineImageNotFoundException,
            FighterImageSettingsNotFoundException {
        var fighter = player.getFighter(0);
        var characterImage = characterImageFetcher.getCharacterImage(fighter, thumbnail);
        if (Game.SSBU.equals(thumbnail.getGame())) {
            smashUltimateCharacterService.convertToAlternateRender(fighter);
        }
        var fighterImageThumbnailSettings = thumbnail.getImageSettings()
                .findFighterImageSettings(fighter.getUrlName());
        characterImage = editCharacterImageWithMask(characterImage, fighterImageThumbnailSettings, player);

        LOGGER.info("Drawing player {}'s character: {}", port, fighter.getName());
        return characterImage;
    }

    private BufferedImage getCharacterPair(Thumbnail thumbnail, Player player, CharacterImageFetcher characterImageFetcher, int port)
            throws MalformedURLException, OnlineImageNotFoundException,
            FighterImageSettingsNotFoundException {
        BufferedImage slot = new BufferedImage(thumbnailWidth/2, thumbnailHeight, BufferedImage.TYPE_INT_ARGB);
        var slotGraphics = slot.getGraphics();
        for (int i=1; i >=0; i--) {
            var fighter = player.getFighter(i);
            var characterImage = characterImageFetcher.getCharacterImage(fighter, thumbnail);
            if (Game.SSBU.equals(thumbnail.getGame())) {
                smashUltimateCharacterService.convertToAlternateRender(fighter);
            }
            var fighterImageThumbnailSettings = thumbnail.getImageSettings()
                    .findFighterImageSettings(fighter.getUrlName());

            var characterImage1 = imageService.resizeImage(characterImage, fighterImageThumbnailSettings.getScale());
            var characterImage2 = Scalr.resize(characterImage1, (int) Math.round(0.7*characterImage1.getWidth()), (int) Math.round(0.7*characterImage1.getHeight()), Scalr.OP_ANTIALIAS);

            //offset + double fighter offset
            int centerOffsetX = (int) Math.round((0.3)*(320-fighterImageThumbnailSettings.getOffset()[0]));
            int centerOffsetY = (int) Math.round((0.3)*(360-fighterImageThumbnailSettings.getOffset()[1]));

            int pairOffsetX = port == 1 ? -100 + 200*(1-i) : -100 + 200*(i);
            int pairOffsetY = -50 + 100*(1-i);

            int flipMultiplier = fighter.isFlip() ? -1 : 1;
            int[] offset = new int[] {
                    fighterImageThumbnailSettings.getOffset()[0] + centerOffsetX + flipMultiplier*pairOffsetX,
                    fighterImageThumbnailSettings.getOffset()[1] + centerOffsetY + pairOffsetY
            };
            var flipCanvas = new BufferedImage(640, 720, BufferedImage.TYPE_INT_ARGB);
            var flipGraphic = flipCanvas.createGraphics();
            flipGraphic.drawImage(characterImage2, offset[0], offset[1], null);
            var flipImage = imageService.flipImage(flipCanvas, fighter.isFlip());
            slotGraphics.drawImage(flipImage, 0, 0, null);
        }
        return slot;
    }

    private BufferedImage getCharacterPairSplit(Thumbnail thumbnail, Player player, CharacterImageFetcher characterImageFetcher, int port)
            throws MalformedURLException, OnlineImageNotFoundException,
            FighterImageSettingsNotFoundException {
        BufferedImage slot = new BufferedImage(thumbnailWidth/2, thumbnailHeight, BufferedImage.TYPE_INT_ARGB);
        var slotGraphics = slot.getGraphics();
        for (int i=1; i >=0; i--) {
            BufferedImage mask;
            try {
                if (port == 1) {
                    mask = ImageIO.read(new File("assets/masks/thumbnails/char" + (i + 1) + ".png"));
                } else {
                    mask = ImageIO.read(new File("assets/masks/thumbnails/char" + (i + 3) + ".png"));
                }
            }catch (Exception e){
                mask = new BufferedImage(thumbnailWidth/2, thumbnailHeight, BufferedImage.TYPE_INT_ARGB);
                mask.getGraphics().fillRect(0, 0, thumbnailWidth/2, thumbnailHeight);
            }

            var fighter = player.getFighter(i);
            var characterImage = characterImageFetcher.getCharacterImage(fighter, thumbnail);
            if (Game.SSBU.equals(thumbnail.getGame())) {
                smashUltimateCharacterService.convertToAlternateRender(fighter);
            }
            var fighterImageThumbnailSettings = thumbnail.getImageSettings()
                    .findFighterImageSettings(fighter.getUrlName());

            var characterImage1 = imageService.resizeImage(characterImage, fighterImageThumbnailSettings.getScale());
            var characterImage2 = Scalr.resize(characterImage1, (int) Math.round(0.7*characterImage1.getWidth()), (int) Math.round(0.7*characterImage1.getHeight()), Scalr.OP_ANTIALIAS);

            //offset + double fighter offset
            int centerOffsetX = (int) Math.round((0.3)*(320-fighterImageThumbnailSettings.getOffset()[0]));
            int centerOffsetY = (int) Math.round((0.3)*(360-fighterImageThumbnailSettings.getOffset()[1]));

            int pairOffsetX = port == 1 ? -100 + 200*(1-i) : -100 + 200*(i);
            int pairOffsetY = -50 + 100*(1-i);

            int flipMultiplier = fighter.isFlip() ? -1 : 1;
            int[] offset = new int[] {
                    fighterImageThumbnailSettings.getOffset()[0] + centerOffsetX + flipMultiplier*pairOffsetX,
                    fighterImageThumbnailSettings.getOffset()[1] + centerOffsetY + pairOffsetY
            };
            var flipCanvas = new BufferedImage(640, 720, BufferedImage.TYPE_INT_ARGB);
            var flipGraphic = flipCanvas.createGraphics();
            flipGraphic.drawImage(characterImage2, offset[0], offset[1], null);
            var flipImage = imageService.flipImage(flipCanvas, fighter.isFlip());
            var maskedImage = imageService.applyMask(flipImage, mask, new int[]{0,0});
            slotGraphics.drawImage(maskedImage, 0, 0, null);
        }
        return slot;
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

    public BufferedImage editCharacterImageWithMask(BufferedImage characterImage , FighterImageThumbnailSettings fighterImageThumbnailSettings, Player player) {
        var isFlip = player.getFighter(0).isFlip() !=
                fighterImageThumbnailSettings.isFlip();
        try {
            var mask = ImageIO.read(new File("assets/masks/thumbnails/default.png"));
            var characterImage1 = imageService.resizeImage(characterImage, fighterImageThumbnailSettings.getScale());

            //var cropOffset = - Math.max((characterImage1.getWidth() + Math.abs(2*fighterImageThumbnailSettings.getOffset()[0]) - 640) / 2, 0);
            //var offsetTotal = new int[]{Math.max(2*fighterImageThumbnailSettings.getOffset()[0], 0) + cropOffset, fighterImageThumbnailSettings.getOffset()[1]};
            var offsetTotal = fighterImageThumbnailSettings.getOffset();
            var doublesScale = 0.7;
            var characterImage3 = imageService.applyMask(characterImage1, mask, offsetTotal);
            var characterImage4 = imageService.flipImage(characterImage3, isFlip);
            //var characterImage5 = imageService.resizeImage(characterImage4, doublesScale);
            return characterImage4;
        } catch (IOException ex){
            LOGGER.error(ex);
            AlertFactory.displayError("ERROR Mask");
        }
        return characterImage;
    }

    private void drawText(Thumbnail thumbnail, Graphics2D g2d)
            throws FontNotFoundException {
        g2d.drawImage(textService.convert(thumbnail.getPlayers().get(0).getPlayerName(),
                fileThumbnailSettings.getTextSettings(), true),
                0, fileThumbnailSettings.getTextSettings().getDownOffsetTop()[0], null);
        g2d.drawImage(textService.convert(thumbnail.getPlayers().get(1).getPlayerName(),
                fileThumbnailSettings.getTextSettings(), true),
                thumbnailWidth / 2,  fileThumbnailSettings.getTextSettings().getDownOffsetTop()[1], null);

        g2d.drawImage(textService.convert(thumbnail.getRound(), fileThumbnailSettings.getTextSettings(), false),
                0, thumbnailHeight - 100 + fileThumbnailSettings.getTextSettings().getDownOffsetBottom()[0], null);
        g2d.drawImage(textService.convert(thumbnail.getDate(), fileThumbnailSettings.getTextSettings(), false),
                thumbnailWidth / 2, thumbnailHeight - 100 + fileThumbnailSettings.getTextSettings().getDownOffsetBottom()[1], null);
    }
}

