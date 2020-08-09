package extension;

import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import fighter.Fighter;
import placement.Placement;
import ui.placement.PlacementPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Top8Chart {
    static int WIDTH = 659;
    static int HEIGHT = 842;

    static BufferedImage chart;
    static Graphics2D g2d;

    static String FIGHTERS_URL = "https://www.smashbros.com/assets_v2/img/fighter/";
    static String FIGHTERS_URL_2 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/renders/";
    static String SANS_URL = "https://i.redd.it/n2tcplon8qk31.png";

    static String FIGHTERS_ICON_URL = "https://www.smashbros.com/assets_v2/img/fighter/pict/";
    static String FIGHTERS_ICON_URL_2 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/stock-icons/png/";
    static String FIGHTERS_ICON_URL_3 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/stock-icons/svg/";

    static String foregroundPath = "assets/images/others/";
    static String localFightersPath = "assets/fighters/";
    static String localFightersIconsPath = "assets/fighters/icons/";

    static String saveThumbnailsPath = "charts/";
    boolean saveLocally;

    public void generateChart(String leagueId, String template, boolean saveLocally, Integer tournamentNumber, Integer participants, String date, ArrayList<PlacementPane> placements)
            throws LocalImageNotFoundException, OnlineImageNotFoundException, FontNotFoundException {

        String thumbnailFileName = leagueId +"-"+ tournamentNumber +".png";

        this.saveLocally = saveLocally;
        chart = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = chart.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        //drawElement(backgroundPath);
        drawElement(foregroundPath+template);

        ArrayList<Placement> places = new ArrayList<>();

        for (PlacementPane pp : placements){
            places.add(pp.generatePlacement());
        }

        int port = 0;
        int portZ = 0;
        for (Placement p : places) {
            port++;
            portZ = 0;
            for (Fighter f : p.getFighters()){
                if (portZ == 0) {
                    /*BufferedImage image = getFighterImage(f);
                    FighterImage fighterImage = new FighterImage(f, image);
                    fighterImage.editImage(port, portZ);
                    if (fighterImage.getImage().getWidth() < WIDTH / 2 && fighterImage.getFighter().isFlip()) {
                        g2d.drawImage(fighterImage.getImage(), null, WIDTH / 2 * port - fighterImage.getImage().getWidth(), 0);
                    } else {
                        g2d.drawImage(fighterImage.getImage(), null, WIDTH / 2 * (port - 1), 0);
                    }*/
                } else if (f.getUrlName() != null) {
                    BufferedImage imageI = getFighterIcon(f);
                    IconImage iconImage = new IconImage(f, imageI);
                    iconImage.editImage(leagueId, port, portZ, p.getPlacementNumber());

                    g2d.drawImage(iconImage.getImage(), null, 60, 0);

                    /*if (iconImage.getImage().getWidth() < WIDTH / 2 && iconImage.getFighter().isFlip()) {
                        g2d.drawImage(iconImage.getImage(), null, WIDTH / 2 * port - iconImage.getImage().getWidth(), 0);
                    } else {
                        g2d.drawImage(iconImage.getImage(), null, WIDTH / 2 * (port - 1), 0);
                    }*/
                }

                portZ++;
            }

            g2d.drawImage(TextToImage.convert(p.getPlayerName(), 90), 0, 10, null);
        }

        g2d.drawImage(TextToImage.convert(tournamentNumber.toString(), 75), 0, HEIGHT - 100, null);
        g2d.drawImage(TextToImage.convert(participants.toString() + " PARTICIPANTS", 75), 0, HEIGHT - 100, null);
        g2d.drawImage(TextToImage.convert(date, 75), WIDTH / 2, HEIGHT - 105, null);

        File dirThumbnails = new File(saveThumbnailsPath);
        if (!dirThumbnails.exists()) dirThumbnails.mkdir();
        saveImage(chart, new File(saveThumbnailsPath +
                thumbnailFileName));
    }


    private void drawElement(String pathname) throws LocalImageNotFoundException {
        try {
            g2d.drawImage(ImageIO.read(new FileInputStream(pathname)), 0, 0, null);
        } catch (IOException e) {
            throw new LocalImageNotFoundException(pathname);
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

    BufferedImage getFighterImage(Fighter fighter) throws OnlineImageNotFoundException {
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

    BufferedImage getFighterIcon(Fighter fighter) throws OnlineImageNotFoundException {
        if (saveLocally) {
            File directory = new File(localFightersIconsPath);
            String fighterDirPath = localFightersIconsPath + fighter.getUrlName() + "/";
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
            image = getFighterIconOnline(fighter);
            saveImage(image, localImage);
            return image;
        } else {
            return getFighterIconOnline(fighter);
        }
    }

    private  BufferedImage getFighterImageOnline(Fighter fighter) throws OnlineImageNotFoundException {
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

    private  BufferedImage getFighterIconOnline(Fighter fighter) throws OnlineImageNotFoundException {
        String urlString = null;
        try {
            urlString = FIGHTERS_ICON_URL + fighter.getUrlName() + ".png";

            URL url = new URL(urlString);
            return ImageIO.read(url);

        } catch (IOException e) {
            throw new OnlineImageNotFoundException(urlString);
        }
    }
}
