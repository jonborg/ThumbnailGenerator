package thumbnail.text;

import exception.FontNotFoundException;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.IOException;
import java.io.InputStream;

public class TextToImage {

    private static int WIDTH = 640;
    private static int HEIGHT = 110;

    private static TextSettings textSettings;
    private static Font font;
    private static boolean top;

    public static BufferedImage convert(String text, TextSettings settings, boolean topText) throws FontNotFoundException {
        textSettings = settings;
        top=topText;

        if (text.isEmpty()) return null;
        BufferedImage rect = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = rect.createGraphics();

        if (textSettings.hasShadow())
            graphics.drawImage(generateText(text,Color.BLACK,
                    top ? textSettings.getSizeTop() : textSettings.getSizeBottom()),5,5,null);
        graphics.drawImage(generateText(text,Color.WHITE,
                top ? textSettings.getSizeTop() : textSettings.getSizeBottom()),0,0,null);
        return rect;
    }


    private static BufferedImage generateText(String text, Color color, int fontSize) throws FontNotFoundException {
        try {
            InputStream fontFile = TextToImage.class.getResourceAsStream("/fonts/" + textSettings.getFont() + ".ttf");
            if (!textSettings.hasBold() && textSettings.hasItalic())
                font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.ITALIC, fontSize);
            if (textSettings.hasBold() && !textSettings.hasItalic())
                font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, fontSize);
            if (textSettings.hasBold() && textSettings.hasItalic())
                font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD + Font.ITALIC, fontSize);
        }catch (FontFormatException | IOException | NullPointerException e){
            System.out.println("Could not find font " + textSettings.getFont() + " on resources. Checking system fonts...");
            if (!textSettings.hasBold() && textSettings.hasItalic())
                font = new Font(textSettings.getFont(), Font.ITALIC,fontSize);
            if (textSettings.hasBold() && !textSettings.hasItalic())
                font = new Font(textSettings.getFont(), Font.BOLD,fontSize);
            if (textSettings.hasBold() && textSettings.hasItalic())
                font = new Font(textSettings.getFont(), Font.BOLD+Font.ITALIC, fontSize);
            //throw new FontNotFoundException(textSettings.getFont());
        }

        BufferedImage rect = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = rect.createGraphics();

        FontRenderContext frc = graphics.getFontRenderContext();
        font.layoutGlyphVector(frc, text.toCharArray(), 0, text.length(), Font.LAYOUT_LEFT_TO_RIGHT);

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
        //else drawOutline(text,graphics);
        graphics.drawString(text, x, y);
        //outline
        if (textSettings.getContour()>0) drawOutline(graphics, text, x, y);
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
        double rotationRequired = Math.toRadians (top ? textSettings.getAngleTop() : textSettings.getAngleBottom());
        double locationX = finalRect.getWidth() / 2;
        double locationY = finalRect.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        Graphics2D g2d = finalRect.createGraphics();
        g2d.drawImage(op.filter(rect, null), 0, 0, null);
        return finalRect;
    }

    private static  void drawOutline(Graphics2D graphics, String text, int x, int y){
        GlyphVector gv = font.createGlyphVector(graphics.getFontRenderContext(), text);
        Shape shape = gv.getOutline();
        graphics.setColor(Color.black);
        graphics.setStroke(new BasicStroke(textSettings.getContour()));
        graphics.translate(x,y);
        graphics.draw(shape);
    }


}
