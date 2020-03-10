import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

import javafx.scene.control.Alert;
import org.imgscalr.Scalr;

public class Thumbnail {
    static int WIDTH = 1280;
    static int HEIGHT = 720;

    static BufferedImage thumbnail;
    static Graphics2D g2d;

    static String FIGHTERS_URL = "https://www.smashbros.com/assets_v2/img/fighter/";
    static String FIGHTERS_URL_2 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/renders/";
    static String SANS_URL = "https://i.redd.it/n2tcplon8qk31.png";

    static String foregroundPath = "resources/images/others/";
    static String backgroundPath = foregroundPath + "background.png";
    static String localFightersPath = "resources/fighters/";

    static String saveThumbnailsPath = "thumbnails/";
    boolean saveLocally;

    public void generateThumbnail(String foreground, boolean saveLocally, String round, String date, Fighter... fighters) {
        this.saveLocally = saveLocally;
        thumbnail = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = thumbnail.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);


        drawElement(backgroundPath);
        int port = 0;
        for (Fighter f : fighters) {
            port++;
            FighterImage fighterImage = new FighterImage(f, getFighterImage(f));
            int offset = 100;

            fighterImage.editImage(port);
            if (fighterImage.getImage().getWidth()<WIDTH/2 && fighterImage.getFighter().isFlip()) {
                g2d.drawImage(fighterImage.getImage(), null, WIDTH / 2 * port - fighterImage.getImage().getWidth(), 0);
            }else{
                g2d.drawImage(fighterImage.getImage(), null, WIDTH / 2 * (port - 1), 0);
            }
        }

        drawElement(foregroundPath+foreground);

        g2d.drawImage(TextToImage.convert(fighters[0].getPlayerName(), 90), 0 , 10, null);
        g2d.drawImage(TextToImage.convert(fighters[1].getPlayerName(), 90), WIDTH / 2 , 5, null);


        g2d.drawImage(TextToImage.convert(round, 75), 0, HEIGHT - 100, null);
        g2d.drawImage(TextToImage.convert(date, 75), WIDTH / 2, HEIGHT - 105, null);

        File dirThumbnails = new File(saveThumbnailsPath);
        if (!dirThumbnails.exists()) dirThumbnails.mkdir();
        saveImage(thumbnail, new File(saveThumbnailsPath+
                fighters[0].getPlayerName()+"-"+fighters[0].getUrlName()+fighters[0].getAlt()+" "+
                fighters[1].getPlayerName()+"-"+fighters[1].getUrlName()+fighters[1].getAlt()+" "+
                round+" "+date.replace("/","_")+".png"));
    }


    private  BufferedImage getFighterImageOnline(Fighter fighter) {
        try {
            String urlString;
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

        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not read URL as image");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Could not find image online");

            alert.showAndWait();
        }
        return null;
    }


    private void drawElement(String pathname) {
        try {
            g2d.drawImage(ImageIO.read(new FileInputStream(pathname)), 0, 0, null);
        } catch (IOException e) {
            System.out.println("Could not find location for image in path " + getClass().getResource(pathname).getPath());
        }
    }

    void saveImage(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "png", file);
            System.out.println("Image saved!");
        } catch (IOException ioe) {
            System.out.println("Image could not be saved: " + ioe.getMessage());
        }
    }

    BufferedImage getFighterImage(Fighter fighter) {
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

            image = getFighterImageOnline(fighter);
            saveImage(image, localImage);
            return image;
        } else {
            return getFighterImageOnline(fighter);
        }
    }
}

