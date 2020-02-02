import javafx.scene.control.Alert;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class FighterImage {
    private Fighter fighter;
    private BufferedImage image;

    private final String scalerPath = "resources/config/scale.txt";
    private final String offsetPath = "resources/config/offset.txt";

    public int offsetX=0;
    public int offsetY=0;


    public FighterImage(Fighter fighter, BufferedImage image){
        this.fighter = fighter;
        this.image = image;
        MultiFighters();
    }

    public BufferedImage editImage( int port){
        image = this.resizeImage(fighter.getUrlName()); //later, use urlname to find mult in a file
        image = this.offsetImage(fighter.getUrlName(),fighter.isFlip(),port);
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR");
            alert.setContentText("Could not detect scaler.txt");
            alert.showAndWait();
        }

        int width = (int) (mult * image.getWidth());
        int height = (int) (mult * image.getHeight());
        //int width = (int) (mult * image.getWidth());
        //int height = (int) (width / image.getWidth() * image.getHeight());

        System.out.println("Resize complete "+image.getWidth() + " "+ image.getHeight());

        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, image.getHeight() < image.getWidth() ? Scalr.Mode.FIT_TO_HEIGHT : Scalr.Mode.FIT_TO_WIDTH,
                Math.max(width, height), Math.max(width, height), Scalr.OP_ANTIALIAS);
        //return Scalr.resize(image, width, height, Scalr.OP_ANTIALIAS);
    }




    private BufferedImage offsetImage(String urlName, boolean flip, int port){
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR");
            alert.setContentText("Could not detect offset.txt");
            alert.showAndWait();        }


        BufferedImage img = new BufferedImage(image.getWidth() + Math.abs(offsetX),
                                                image.getHeight() + Math.abs(offsetY),
                                                    BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        int inputX=0;
        int inputY=0;
       /* if (offsetX>0 && offsetY>0){
            g2d.drawImage(image,offsetX,offsetY,null);
        }
        if (offsetX>0 && offsetY<=0){
            image=cropImageY(image,-offsetY);
            g2d.drawImage(image,offsetX,0,null);
        }
        if (offsetX<=0 && offsetY>0){
            image=cropImageX(image,-offsetX);
            g2d.drawImage(image,0,offsetY,null);
        }
        if (offsetX<=0 && offsetY<=0){
            image=cropImageY(image,-offsetY);
            image=cropImageX(image,-offsetX);
            g2d.drawImage(img,0,0,null);
        }*/
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
          //  marginY = (height-heightLimit)/2;
          //  marginY = (height-heightLimit)/2;
        }
        return Scalr.crop(image, marginX,marginY,Math.min(width,widthLimit), Math.min(height,heightLimit), null);
    }

    private BufferedImage cropImageX(BufferedImage img, int x){
        int width = img.getWidth();
        int height = img.getHeight();

        return Scalr.crop(img,x,0,width-x,height,null);
    }

    private BufferedImage cropImageY(BufferedImage img, int y){
        int width = img.getWidth();
        int height = img.getHeight();

        return Scalr.crop(img,0,y,width,height-y,null);
    }

    private BufferedImage cropImageY(int widthLimit,int heightLimit){
        int width = image.getWidth();
        int height = image.getHeight();
        int marginX = 0;
        int marginY= 0;

        if (height>heightLimit) {
            //marginX = (width-widthLimit)/2;
        }

        return Scalr.crop(image, 0,marginY,Math.min(width,widthLimit), Math.min(height,heightLimit), null);
    }


    private void MultiFighters(){
        if (fighter.getUrlName().equals("pokemon_trainer") ||
                fighter.getUrlName().equals("villager") ||
                fighter.getUrlName().equals("robin") ||
                fighter.getUrlName().equals("corrin") ||
                fighter.getUrlName().equals("byleth")){
            if (fighter.getAlt() % 2==0){
                fighter.setUrlName(fighter.getUrlName()+"F");
            }else{
                fighter.setUrlName(fighter.getUrlName()+"M");
            }
            return;
        }

        if (fighter.getUrlName().equals("wii_fit_trainer") ||
                fighter.getUrlName().equals("inkling")){
            if (fighter.getAlt() % 2==0){
                fighter.setUrlName(fighter.getUrlName()+"M");
            }else{
                fighter.setUrlName(fighter.getUrlName()+"F");
            }
            return;
        }

        if (fighter.getUrlName().equals("cloud")){
            if (fighter.getAlt() % 2==0){
                fighter.setUrlName("cloudAC");
            }
            return;
        }

        if (fighter.getUrlName().equals("ike")){
            if (fighter.getAlt() % 2==0){
                fighter.setUrlName("ike2");
            }else{
                fighter.setUrlName("ike1");
            }
            return;
        }

        if (fighter.getUrlName().equals("olimar")){
            if (fighter.getAlt() > 4 ){
                fighter.setUrlName("alph");
            }
            return;
        }

        if (fighter.getUrlName().equals("mii_gunner")){
            if (fighter.getAlt() == 2){
                fighter.setUrlName("sans");
            }
            return;
        }

        if (fighter.getUrlName().equals("bowser_jr")){
            fighter.setUrlName("bowser_jr"+fighter.getAlt());
            return;
        }

        if (fighter.getUrlName().equals("bayonetta")){
            if (fighter.getAlt() % 2==0){
                fighter.setUrlName("bayonetta1");
            }else{
                fighter.setUrlName("bayonetta2");
            }
            return;
        }

        if (fighter.getUrlName().equals("dq_hero")){
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

        if (fighter.getUrlName().equals("joker")){
            //1 to 6
            if (fighter.getAlt()<7) fighter.setUrlName("joker1");
            //7 and 8
            else fighter.setUrlName("joker2");
            return;
        }

        if (fighter.getUrlName().equals("falco")){
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
