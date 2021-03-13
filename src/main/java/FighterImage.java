import fighter.Fighter;
import org.imgscalr.Scalr;
import ui.factory.alert.AlertFactory;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FighterImage {
    private Fighter fighter;
    private BufferedImage image;

    private final String scalerPath = "settings/thumbnails/images/scale.txt";
    private final String offsetPath = "settings/thumbnails/images/offset.txt";


    AlertFactory alertFactory = new AlertFactory();

    public FighterImage(Fighter fighter, BufferedImage image){
        this.fighter = fighter;
        this.image = image;
        MultiFighters(fighter);
    }

    public BufferedImage editImage( int port){
        image = this.resizeImage(fighter.getUrlName()); //later, use urlname to find mult in a file
        image = this.offsetImage(fighter.getUrlName());
        image = this.flipImage(fighter.isFlip());
        image = this.cropImage(1280/2,720);
        return image;
    }




    private  BufferedImage flipImage(boolean flip){
        if (flip) {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx,
                    AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            return op.filter(image, null);
        }
        return image;
    }



    private BufferedImage resizeImage(String urlName) {
        System.out.println("Performing resize of image with size: "+image.getWidth()+ " "+ image.getWidth());

        double mult = 1;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(scalerPath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(urlName)){
                    String[] words = line.split(" ");
                    if (words.length>1)
                        mult = Double.parseDouble(words[1]);
                    break;
                }
            }
        }catch(IOException e){
            alertFactory.displayError("Could not detect scaler.txt");
        }

        int width = (int) (mult * image.getWidth());
        int height = (int) (mult * image.getHeight());

        System.out.println("Resize complete "+image.getWidth() + " "+ image.getHeight());

        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, image.getHeight() < image.getWidth() ? Scalr.Mode.FIT_TO_HEIGHT : Scalr.Mode.FIT_TO_WIDTH,
                Math.max(width, height), Math.max(width, height), Scalr.OP_ANTIALIAS);
    }




    private BufferedImage offsetImage(String urlName){
        int offsetX = 0;
        int offsetY = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(offsetPath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(urlName)){
                    String[] words = line.split(" ");
                    if (words.length>1)
                        offsetX = 2* Integer.parseInt(words[1]);
                    if (words.length>2)
                        offsetY = Integer.parseInt(words[2]);
                    break;
                }
            }
        }catch(IOException e){
            alertFactory.displayError("Could not detect offset.txt");
        }


        BufferedImage img = new BufferedImage(image.getWidth() + Math.abs(offsetX),
                                                image.getHeight() + Math.abs(offsetY),
                                                    BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        int inputX=0;
        int inputY=0;

        if(offsetX>0) inputX=offsetX;
        if(offsetY>0) {
            inputY=offsetY;
            g2d.drawImage(image,inputX,inputY,null);
        }else{
            g2d.drawImage(cropImageY(image,-offsetY),inputX,0,null);
        }
        return img;
    }




    private BufferedImage cropImage(int widthLimit, int heightLimit){
        int width = image.getWidth();
        int height = image.getHeight();
        int marginX = 0;
        int marginY= 0;

        if (width>widthLimit) {
            marginX = (width-widthLimit)/2;
        }
        System.out.println(image.getWidth()+" "+image.getHeight());
        if (height>heightLimit) {

        }
        return Scalr.crop(image, marginX,marginY,Math.min(width,widthLimit), Math.min(height,heightLimit), null);
    }


    private BufferedImage cropImageY(BufferedImage img, int y){
        int width = img.getWidth();
        int height = img.getHeight();

        return Scalr.crop(img,0,y,width,height-y,null);
    }

    static public void MultiFighters(Fighter fighter){
        if ("pokemon_trainer".contains(fighter.getUrlName())) {
            if (fighter.getAlt() % 2 == 0) fighter.setUrlName("pokemon_trainerF");
            else fighter.setUrlName("pokemon_trainerM");
            return;
        }

        if ("villager".contains(fighter.getUrlName())){
            if (fighter.getAlt() % 2==0)  fighter.setUrlName("villagerF");
            else fighter.setUrlName("villagerM");
            return;
        }

        if ("robin".contains(fighter.getUrlName()) && !"rob".equals(fighter.getUrlName())){
            if (fighter.getAlt() % 2==0)  fighter.setUrlName("robinF");
            else fighter.setUrlName("robinM");
            return;
        }

        if ("corrin".contains(fighter.getUrlName())){
            if (fighter.getAlt() % 2==0)  fighter.setUrlName("corrinF");
            else fighter.setUrlName("corrinM");
            return;
        }

        if ("byleth".contains(fighter.getUrlName())) {
            if (fighter.getAlt() % 2 == 0) fighter.setUrlName("bylethF");
            else fighter.setUrlName("bylethM");
            return;
        }



        if ("wii_fit_trainer".contains(fighter.getUrlName())) {
            if (fighter.getAlt() % 2 == 0) fighter.setUrlName("wii_fit_trainerM");
            else fighter.setUrlName("wii_fit_trainerF");
            return;
        }

        if ("inkling".contains(fighter.getUrlName())) {
            if (fighter.getAlt() % 2 == 0) fighter.setUrlName("inklingM");
            else fighter.setUrlName("inklingF");
            return;
        }



        if ("cloud".contains(fighter.getUrlName())) {
            if (fighter.getAlt() % 2==0){
                fighter.setUrlName("cloudAC");
            }
            return;
        }

        if ("ike".contains(fighter.getUrlName())) {
            if (fighter.getAlt() % 2==0){
                fighter.setUrlName("ike2");
            }else{
                fighter.setUrlName("ike1");
            }
            return;
        }

        if ("olimar".contains(fighter.getUrlName())) {
            if (fighter.getAlt() > 4 ){
                fighter.setUrlName("alph");
            }
            return;
        }

        if ("mii_gunner".contains(fighter.getUrlName())) {
            if (fighter.getAlt() == 2){
                fighter.setUrlName("sans");
            }
            return;
        }

        if (!"bowser".equals(fighter.getUrlName()) && "bowser_jr".contains(fighter.getUrlName())) {
            fighter.setUrlName("bowser_jr"+fighter.getAlt());
            return;
        }

        if ("bayonetta".contains(fighter.getUrlName())) {
            if (fighter.getAlt() % 2==0){
                fighter.setUrlName("bayonetta1");
            }else{
                fighter.setUrlName("bayonetta2");
            }
            return;
        }

        if ("dq_hero".contains(fighter.getUrlName())) {
            //1 and 5
            if (fighter.getAlt() % 4 == 1) fighter.setUrlName("dq_hero1");
            //2 and 6
            if (fighter.getAlt() % 4 == 2) fighter.setUrlName("dq_hero2");
            //3 and 7
            if (fighter.getAlt() % 4 == 3) fighter.setUrlName("dq_hero3");
            //4 and 8
            if (fighter.getAlt() % 4 == 0) fighter.setUrlName("dq_hero4");
            return;
        }

        if ("joker".contains(fighter.getUrlName())) {
            //1 to 6
            if (fighter.getAlt()<7) fighter.setUrlName("joker1");
            //7 and 8
            else fighter.setUrlName("joker2");
            return;
        }

        if ("sephiroth".contains(fighter.getUrlName())) {
            //1 to 6
            if (fighter.getAlt()<7) fighter.setUrlName("sephiroth1");
            //7 and 8
            else fighter.setUrlName("sephiroth2");
            return;
        }

        if ("falco".contains(fighter.getUrlName())) {
            fighter.setUrlName("falco ");
        }


    }


    public Fighter getFighter(){
        return this.fighter;
    }

    public void setFighter(Fighter fighter){
        this.fighter = fighter;
    }

    public BufferedImage getImage(){
        return this.image;
    }

    public void setFighter(BufferedImage image){
        this.image = image;
    }



}
