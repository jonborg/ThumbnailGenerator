package thumbnail.generate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import fighter.Fighter;
import fighter.FighterImage;
import thumbnail.text.TextToImage;
import tournament.Tournament;


public class Thumbnail {
    private static int WIDTH = 1280;
    private static int HEIGHT = 720;

    private static BufferedImage thumbnail;
    private static Graphics2D g2d;

    private static String FIGHTERS_URL = "https://www.smashbros.com/assets_v2/img/fighter/";
    private static String FIGHTERS_URL_2 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/renders/";
    private static String SANS_URL = "https://i.redd.it/n2tcplon8qk31.png";

    private static String localFightersPath = "assets/fighters/";

    private static String saveThumbnailsPath = "thumbnails/";
    private static boolean saveLocally;


    public static void generateAndSaveThumbnail(Tournament tournament, boolean locally, String round, String date, Fighter... fighters)
            throws LocalImageNotFoundException, OnlineImageNotFoundException, FontNotFoundException{
        generateThumbnail(tournament, locally,round ,date, fighters);
        saveThumbnail(round, date, fighters);
    }

    public static BufferedImage generatePreview(Tournament tournament)
            throws LocalImageNotFoundException, OnlineImageNotFoundException, FontNotFoundException {
        List<Fighter> fighters = Fighter.generatePreviewFighters();
        return generateThumbnail(tournament, false, "Pools Round 1" ,"07/12/2018",
                fighters.get(0), fighters.get(1));
    }



    private static BufferedImage generateThumbnail(Tournament tournament, boolean locally, String round, String date, Fighter... fighters)
            throws LocalImageNotFoundException, OnlineImageNotFoundException, FontNotFoundException {

        saveLocally = locally;
        thumbnail = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = thumbnail.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawElement(tournament.getThumbnailBackground());
        int port = 0;
        for (Fighter f : fighters) {
            port++;
            BufferedImage image = getFighterImage(f);
            FighterImage fighterImage = new FighterImage(f, image);
            fighterImage.editImage(port);
            if (fighterImage.getImage().getWidth() < WIDTH / 2 && fighterImage.getFighter().isFlip()) {
                g2d.drawImage(fighterImage.getImage(), null, WIDTH / 2 * port - fighterImage.getImage().getWidth(), 0);
            } else {
                g2d.drawImage(fighterImage.getImage(), null, WIDTH / 2 * (port - 1), 0);
            }

        }

        drawElement(tournament.getThumbnailForeground());

        g2d.drawImage(TextToImage.convert(fighters[0].getPlayerName(), tournament.getTextSettings(), true),
                0, tournament.getTextSettings().getDownOffsetTop()[0], null);
        g2d.drawImage(TextToImage.convert(fighters[1].getPlayerName(), tournament.getTextSettings(), true),
                WIDTH / 2,  tournament.getTextSettings().getDownOffsetTop()[1], null);


        g2d.drawImage(TextToImage.convert(round, tournament.getTextSettings(), false),
                0, HEIGHT - 100 + tournament.getTextSettings().getDownOffsetBottom()[0], null);
        g2d.drawImage(TextToImage.convert(date, tournament.getTextSettings(), false),
                WIDTH / 2, HEIGHT - 100 + tournament.getTextSettings().getDownOffsetBottom()[1], null);
        return thumbnail;
    }

    private static void saveThumbnail(String round, String date, Fighter... fighters){
        String thumbnailFileName = fighters[0].getPlayerName().replace("|","_")+"-"+fighters[0].getUrlName()+fighters[0].getAlt()+"--"+
                fighters[1].getPlayerName().replace("|","_")+"-"+fighters[1].getUrlName()+fighters[1].getAlt()+"--"+
                round+"-"+date.replace("/","_")+".png";

        File dirThumbnails = new File(saveThumbnailsPath);
        if (!dirThumbnails.exists()) dirThumbnails.mkdir();
        saveImage(thumbnail, new File(saveThumbnailsPath +
                thumbnailFileName));
    }

    private static void drawElement(String pathname) throws LocalImageNotFoundException {
        try {
            g2d.drawImage(ImageIO.read(new FileInputStream(pathname)), 0, 0, null);
        } catch (IOException e) {
            throw new LocalImageNotFoundException(pathname);
        }
    }

    static void saveImage(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "png", file);
            System.out.println("Image saved!");
        } catch (IOException ioe) {
            System.out.println("Image could not be saved: " + ioe.getMessage());
        }
    }

    static BufferedImage getFighterImage(Fighter fighter) throws OnlineImageNotFoundException {
        if (saveLocally) {
            File directory = new File(localFightersPath);
            String fighterDirPath = localFightersPath + fighter.getUrlName() + "/";
            File fighterDir = new File(fighterDirPath);
            BufferedImage image;

            if (!directory.exists()) directory.mkdir();
            if (!fighterDir.exists()) fighterDir.mkdir();

            File localImage = new File(fighterDirPath + fighter.getAlt()+".png");
            try {
                image = ImageIO.read(localImage);
                return image;
            } catch (IOException e) {
                System.out.println("Image for " + fighter.getUrlName() + fighter.getAlt() + " does not exist locally. Will try finding online");
            }

            //if cannot find locally, will try to find online
            image = getFighterImageOnline(fighter);
            saveImage(image, localImage);
            return image;
        } else {
            return getFighterImageOnline(fighter);
        }
    }

    private static BufferedImage getFighterImageOnline(Fighter fighter) throws OnlineImageNotFoundException {
        String urlString = null;
        try {
            if (fighter.getAlt() == 1) {
                urlString = FIGHTERS_URL + fighter.getUrlName() + "/main.png";
            } else {
                urlString = FIGHTERS_URL + fighter.getUrlName() + "/main" + fighter.getAlt() + ".png";
            }

            if (fighter.getUrlName().contains("pokemon_trainer")) {
                urlString = FIGHTERS_URL_2 + "misc/pokemon-trainer-0" + fighter.getAlt() + ".png";
            }
            if (fighter.getUrlName().contains("mii_brawler")) {
                urlString = FIGHTERS_URL_2 + "fighters/51/01.png";
            }
            if (fighter.getUrlName().contains("mii_swordfighter")) {
                urlString = FIGHTERS_URL_2 + "fighters/52/01.png";
            }
            if (fighter.getUrlName().contains("mii_gunner")) {
                if (fighter.getAlt() == 1) urlString = FIGHTERS_URL_2 + "fighters/53/01.png";
                if (fighter.getAlt() == 2) urlString = SANS_URL;
            }


            URL url = new URL(urlString);
            return ImageIO.read(url);

        } catch (IOException e) {
            throw new OnlineImageNotFoundException(urlString);
        }
    }
}

