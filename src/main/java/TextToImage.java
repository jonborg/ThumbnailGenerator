import javafx.scene.effect.DropShadow;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class TextToImage {

    private static int WIDTH = 640;
    private static int HEIGHT = 110;
    private static float angle = -2f;


    public static BufferedImage convert(String text, int fontSize) {
        if (text.isEmpty()) return null;
        BufferedImage rect = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = rect.createGraphics();

        graphics.drawImage(generateText(text,Color.BLACK, fontSize),5,5,null);
        graphics.drawImage(generateText(text,Color.WHITE, fontSize),0,0,null);

        return rect;
    }


    private static BufferedImage generateText(String text, Color color, int fontSize){
        Font font = new Font ("Bebas Neue",Font.BOLD + Font.ITALIC, fontSize);

        BufferedImage rect = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = rect.createGraphics();

        FontRenderContext frc = graphics.getFontRenderContext();
        TextLayout textTl = new TextLayout(text, font, frc);

        // Get the FontMetrics
        FontMetrics metrics = graphics.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = (rect.getWidth() - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = ((rect.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setFont(font);
        graphics.setColor(color);

        if (color == Color.BLACK) return blurText(rect,text,graphics,x,y);
        graphics.drawString(text, x, y);

        return rotateText(rect);
    }

    private static BufferedImage blurText(BufferedImage rect, String text,Graphics2D graphics, int x, int y){

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
        graphics.drawString(text, x, y);

        //Not sure if different effect than all = 1/9f
        Kernel kernel = new Kernel(3,3 , new float[]
                { 8/33f, 6/33f, 4/33f,
                        6/33f, 4/33f, 2/33f,
                        4/33f, 2/33f, 1/33f});



        BufferedImageOp op = new ConvolveOp(kernel);

        BufferedImage image = op.filter(rect, null);

        return rotateText(image);
    }

    private static BufferedImage rotateText(BufferedImage rect){
        BufferedImage finalRect = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        double rotationRequired = Math.toRadians (angle);
        double locationX = finalRect.getWidth() / 2;
        double locationY = finalRect.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        Graphics2D g2d = finalRect.createGraphics();
        g2d.drawImage(op.filter(rect, null), 0, 0, null);
        return finalRect;
    }


}
