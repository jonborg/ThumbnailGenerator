package thumbnailgenerator.service;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.TextFont;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.exception.FontNotFoundException;

@Service
public class TextService {
    private static final Logger LOGGER = LogManager.getLogger(TextService.class);

    private static int WIDTH = 640;
    private static int HEIGHT = 110;

    public BufferedImage convert(String text, TextSettings textSettings, boolean isTopText) throws
            FontNotFoundException {
        if (text.isEmpty()) return null;
        BufferedImage rect = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = rect.createGraphics();

        if (textSettings.isShadow()) {
            graphics.drawImage(generateText(text, Color.BLACK,
                    textSettings,
                    isTopText
            ), 5, 5, null);
        }
        graphics.drawImage(generateText(text,Color.WHITE,
                textSettings,
                isTopText
        ),0,0,null);
        return rect;
    }


    private BufferedImage generateText(String text, Color color, TextSettings textSettings, boolean isTopText) throws FontNotFoundException {

        if (color.equals(Color.BLACK)){
            LOGGER.debug("Loading font {} for text {}", textSettings.getFont(), text);
        } else {
            LOGGER.debug("Loading font {} for text {}'s shadow", textSettings.getFont(), text);
        }

        var fontSize = isTopText ? textSettings.getSizeTop() : textSettings.getSizeBottom();
        var textFont = new TextFont(textSettings, fontSize);
        BufferedImage rect = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = rect.createGraphics();

        FontMetrics fontMetrics = graphics.getFontMetrics(textFont.getSelectedFont());
        var textDataList = splitTextByLanguage(text);
        var textWidth = checkTextWidth(textDataList, textFont, graphics);

        // Determine the X coordinate for the text
        int x = (rect.getWidth() - textWidth) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = ((rect.getHeight() - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(color);

        var offsetX = 0;
        for (TextData textData : textDataList) {
            if (textData.getLanguage() == "Japanese") {
                graphics.setFont(textFont.getJapaneseFont());
            } else {
                graphics.setFont(textFont.getSelectedFont());
            }

            if (color == Color.BLACK) {
                LOGGER.debug("Drawing shadow of text {}.", text);
                blurText(rect, textData.text, graphics, x + offsetX, y);
            } else {
                LOGGER.debug("Drawing text {}.", text);
                graphics.drawString(textData.text, x + offsetX, y);

                //outline
                if (textSettings.getContour() > 0) {
                    LOGGER.debug("Drawing text {}'s contour", text);
                    drawOutline(graphics, textSettings, textFont, textData.text, x + offsetX, y);
                }
            }
            offsetX += textData.getWidth();
        }

        LOGGER.debug("Rotate text {}.", text);
        return rotateText(rect, textSettings, isTopText);
    }

    private BufferedImage blurText(BufferedImage rect, String text, Graphics2D graphics, int x, int y){

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
        graphics.drawString(text, x, y);

        //Not sure if different effect than all = 1/9f
        Kernel kernel = new Kernel(3,3 , new float[]
                { 8/33f, 6/33f, 4/33f,
                        6/33f, 4/33f, 2/33f,
                        4/33f, 2/33f, 1/33f});

        BufferedImageOp op = new ConvolveOp(kernel);
        BufferedImage image = op.filter(rect, null);
        return image;
    }

    private BufferedImage rotateText(BufferedImage rect, TextSettings textSettings, boolean isTopText){
        BufferedImage finalRect = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        double rotationRequired = Math.toRadians (isTopText ? textSettings.getAngleTop() : textSettings.getAngleBottom());
        double locationX = (double) finalRect.getWidth() / 2;
        double locationY = (double) finalRect.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        Graphics2D g2d = finalRect.createGraphics();
        g2d.drawImage(op.filter(rect, null), 0, 0, null);
        return finalRect;
    }

    private void drawOutline(Graphics2D graphics, TextSettings textSettings, TextFont textFont, String text, int x, int y){
        GlyphVector gv = textFont.getSelectedFont().createGlyphVector(graphics.getFontRenderContext(), text);
        Shape shape = gv.getOutline();
        graphics.setColor(Color.black);
        graphics.setStroke(new BasicStroke(textSettings.getContour()));
        graphics.translate(x,y);
        graphics.draw(shape);
    }

    private boolean isCharJapanese(char ch) {
        return (ch >= 0x3040 && ch <= 0x309F) || // Hiragana
                (ch >= 0x30A0 && ch <= 0x30FF) || // Katakana
                (ch >= 0x4E00 && ch <= 0x9FFF) || // Kanji
                (ch >= 0x3000 && ch <= 0x303F);  // Punctuation, etc.
    }

    private List<TextData> splitTextByLanguage(String input) {
        List<TextData> result = new ArrayList<>();
        if (input == null || input.isEmpty()) return result;

        StringBuilder current = new StringBuilder();
        boolean currentIsJapanese = isCharJapanese(input.charAt(0));

        for (char ch : input.toCharArray()) {
            boolean isJap = isCharJapanese(ch);
            if (isJap == currentIsJapanese) {
                current.append(ch);
            } else {
                result.add(new TextData(current.toString(), currentIsJapanese ? "Japanese" : "English"));
                current.setLength(0);
                current.append(ch);
                currentIsJapanese = isJap;
            }
        }
        result.add(new TextData(current.toString(), currentIsJapanese ? "Japanese" : "English"));
        return result;
    }

    private int checkTextWidth(List<TextData> textDataList, TextFont textFont, Graphics2D graphics){
        // Get the FontMetrics
        FontMetrics fontMetrics = graphics.getFontMetrics(textFont.getSelectedFont());
        FontMetrics japaneseFontMetrics = graphics.getFontMetrics(textFont.getJapaneseFont());
        var finalWidth = 0;
        for (TextData textData : textDataList) {
            int width;
            if(textData.language == "Japanese"){
                width = japaneseFontMetrics.stringWidth(textData.text);
            } else {
                width = fontMetrics.stringWidth(textData.text);
            }
            textData.setWidth(width);
            finalWidth += width;
        }
        return finalWidth;
    }
}

@Setter
@Getter
class TextData {
    String text;
    String language;
    int width;

    TextData(String text, String language) {
        this.text = text;
        this.language = language;
        this.width = -1;
    }
}