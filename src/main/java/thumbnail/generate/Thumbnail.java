package thumbnail.generate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fighter.Player;
import com.google.gson.reflect.TypeToken;
import exception.FighterImageSettingsNotFoundException;
import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import fighter.DownloadFighterURL;
import fighter.Fighter;
import fighter.FighterArtType;
import file.FileUtils;
import file.json.JSONReader;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import file.json.JSONWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import thumbnail.image.FighterImageThumbnail;
import thumbnail.image.settings.ImageSettings;
import thumbnail.text.TextToImage;
import top8.image.settings.FighterImageTop8Settings;
import top8.image.settings.SlotImageTop8Settings;
import tournament.Tournament;
import ui.factory.alert.AlertFactory;

import static fighter.image.FighterImage.convertToAlternateRender;

public class Thumbnail {
    private static final Logger LOGGER = LogManager.getLogger(Thumbnail.class);

    private static int WIDTH = 1280;
    private static int HEIGHT = 720;

    private static BufferedImage thumbnail;
    private static Graphics2D g2d;

    private static String localFightersPath;
    private static String saveThumbnailsPath = FileUtils.getSaveThumbnailsPath();
    
    private static ThumbnailSettings ts;

    public static void generateAndSaveThumbnail(ThumbnailSettings thumbnailSettings)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException{

        generateThumbnail(thumbnailSettings);
        saveThumbnail();
    }

    public static BufferedImage generatePreview(Tournament tournament, FighterArtType artType)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException {
        LOGGER.info("Generating thumbnail preview.");
        List<Player> players = Player.generatePreviewPlayers();
        ImageSettings imageSettings = (ImageSettings)
                JSONReader.getJSONArrayFromFile(tournament.getThumbnailSettings()
                                .getFighterImageSettingsFile(artType),
                        new TypeToken<ArrayList<ImageSettings>>() {}.getType()).get(0);
        return generateThumbnail(ThumbnailSettings.builder()
                                                .tournament(tournament)
                                                .imageSettings(imageSettings)
                                                .locally(false)
                                                .round("Pools Round 1")
                                                .date("07/12/2018")
                                                .players(players)
                                                .artType(artType)
                                                .build());
    }


    private static BufferedImage generateThumbnail(ThumbnailSettings thumbnailSettings)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException {
        ts = thumbnailSettings;
        LOGGER.debug("*********************************************************************************************");
        LOGGER.debug("Creating thumbnail with following parameters:");
        LOGGER.debug("Tournament -> {}", ts.getTournament().getName());
        LOGGER.debug("Player 1 -> {}", ts.getPlayers().get(0).toString());
        LOGGER.debug("Player 2 -> {}", ts.getPlayers().get(1).toString());
        LOGGER.debug("Round -> {}", ts.getRound());
        LOGGER.debug("Date -> {}", ts.getDate());
        LOGGER.debug("Art used -> {}", ts.getArtType());
        LOGGER.debug("*********************************************************************************************");

        localFightersPath = FileUtils.getLocalFightersPath(ts.getArtType());

        thumbnail = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = thumbnail.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        if(!ts.getTournament().getThumbnailSettings().getBackground().isEmpty()) {
            LOGGER.info("Drawing background in path {}.",
                    ts.getTournament().getThumbnailSettings().getBackground());
            drawElement(
                    ts.getTournament().getThumbnailSettings().getBackground());
        }
        int port = 0;
        for (Player player : ts.getPlayers()) {
            port++;
            LOGGER.info("Drawing player {} information.", port);
            var f = player.getFighter(0);
            var image = getFighterImage(f);
            convertToAlternateRender(f);
           // test(ts.getImageSettings());
            var fighterImageSettings = ts.getImageSettings()
                    .findFighterImageSettings(f.getUrlName());
            var fighterImage = new FighterImageThumbnail(f, fighterImageSettings, image);

            LOGGER.info("Drawing player {}'s character: {}", port, f.getName());
            if (fighterImage.getImage().getWidth() < WIDTH / 2 && fighterImage.getFighter().isFlip()) {
                g2d.drawImage(fighterImage.getImage(), null, WIDTH / 2 * port - fighterImage.getImage().getWidth(), 0);
            } else {
                g2d.drawImage(fighterImage.getImage(), null, WIDTH / 2 * (port - 1), 0);
            }
        }

        if(!ts.getTournament().getThumbnailSettings().getForeground().isEmpty()) {
            LOGGER.info("Drawing thumbnail foreground in path {}.",
                    ts.getTournament().getThumbnailSettings().getForeground());
            drawElement(
                    ts.getTournament().getThumbnailSettings().getForeground());
        }
        LOGGER.info("Drawing thumbnail text");
        LOGGER.debug("Loading {} text settings: {}", ts.getTournament().getName(),
                ts.getTournament().getThumbnailSettings().getTextSettings());
        g2d.drawImage(TextToImage.convert(ts.getPlayers().get(0).getPlayerName(),
                ts.getTournament().getThumbnailSettings().getTextSettings(), true),
                0, ts.getTournament().getThumbnailSettings()
                        .getTextSettings().getDownOffsetTop()[0], null);
        g2d.drawImage(TextToImage.convert(ts.getPlayers().get(1).getPlayerName(),
                ts.getTournament().getThumbnailSettings().getTextSettings(), true),
                WIDTH / 2,  ts.getTournament().getThumbnailSettings()
                        .getTextSettings().getDownOffsetTop()[1], null);

        g2d.drawImage(TextToImage.convert(ts.getRound(), ts.getTournament()
                        .getThumbnailSettings().getTextSettings(), false),
                0, HEIGHT - 100 + ts.getTournament().getThumbnailSettings()
                        .getTextSettings().getDownOffsetBottom()[0], null);
        g2d.drawImage(TextToImage.convert(ts.getDate(), ts.getTournament()
                        .getThumbnailSettings().getTextSettings(), false),
                WIDTH / 2, HEIGHT - 100 + ts.getTournament().getThumbnailSettings()
                        .getTextSettings().getDownOffsetBottom()[1], null);
        return thumbnail;
    }

    private static void saveThumbnail(){
        String thumbnailFileName = ts.getPlayers().get(0).getPlayerName().replace("|","_").replace("/","_")+"-"+
                ts.getPlayers().get(0).getFighter(0).getUrlName()+ts.getPlayers().get(0).getFighter(0).getAlt()+"--"+
                ts.getPlayers().get(1).getPlayerName().replace("|","_").replace("/","_")+"-"+
                ts.getPlayers().get(1).getFighter(0).getUrlName()+ts.getPlayers().get(1).getFighter(0).getAlt()+"--"+
                ts.getRound()+"-"+ts.getDate().replace("/","_")+".png";

        File dirThumbnails = new File(saveThumbnailsPath);
        if (!dirThumbnails.exists()) dirThumbnails.mkdir();
        saveImage(thumbnail, new File(saveThumbnailsPath +
                thumbnailFileName));
    }

    private static void drawElement(String pathname) throws LocalImageNotFoundException {
        try {
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream(pathname));
            g2d.drawImage(bufferedImage, 0, 0, null);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("An issue occurred when loading image in path {}:", pathname);
            LOGGER.catching(e);
            throw new LocalImageNotFoundException(pathname);
        }
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

    static BufferedImage getFighterImage(Fighter fighter) throws OnlineImageNotFoundException {
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

  /*  private static void test(ImageSettings imageSettings){
        var list = imageSettings.getFighterImages();
        top8.image.settings.ImageSettings top8ImageSettings = new top8.image.settings.ImageSettings();
        List<FighterImageTop8Settings> fighterImages = new ArrayList<>();
        for (var el: list){
            List<SlotImageTop8Settings> slotlist = new ArrayList<>();
            for (int i=1;i<=8; i++){
                var a = el.getOffset();
                for (int j = 0; j < a.length; j++){
                    a[j] *= 0.4f;
                }
                SlotImageTop8Settings slot = new SlotImageTop8Settings(a, 0.4f, el.isFlip(), i);
                slotlist.add(slot);
            }
            FighterImageTop8Settings fighterImageTop8Settings =
                    new FighterImageTop8Settings(el.getFighter(), slotlist);
            fighterImages.add(fighterImageTop8Settings);
        }
        top8ImageSettings.setFighterImages(fighterImages);
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter("settings/top8/images/icarus.json")) {
            String json = gson.toJson(top8ImageSettings);
            LOGGER.debug("Writing json to file {} -> {}", "settings/top8/images/icarus.json", json);
            writer.write(json);
        } catch (FileNotFoundException e) {
            AlertFactory.displayError("FileNotFoundException", ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }

    }*/
}

