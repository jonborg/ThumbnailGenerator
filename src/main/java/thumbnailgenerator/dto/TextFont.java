package thumbnailgenerator.dto;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnailgenerator.service.TextService;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

@Getter
public class TextFont {

    private static final Logger LOGGER = LogManager.getLogger(TextService.class);
    private Font selectedFont;
    private Font japaneseFont;

    public TextFont(TextSettings textSettings, int fontSize){
        var style = selectStyle(textSettings);
        selectedFont = generateFont(textSettings.getFont(), textSettings, style, fontSize);
        japaneseFont = generateFont("Meiryo", textSettings, style, fontSize);
    }

    private Font generateFont(String fontName, TextSettings textSettings, int style, int fontSize) {
        try{
            var tffFileStream = TextFont.class.getResourceAsStream("/fonts/" + fontName + ".ttf");
            return Font.createFont(Font.TRUETYPE_FONT, tffFileStream).deriveFont(style, fontSize);
        } catch (FontFormatException | IOException | NullPointerException e) {
            LOGGER.debug("Could not find font {} on resources. Checking system fonts.", fontName);
            return new Font(fontName, style, fontSize);
        }
    }

    private int selectStyle(TextSettings textSettings){
        if (!textSettings.isBold() && textSettings.isItalic())
            return Font.ITALIC;
        else if (textSettings.isBold() && !textSettings.isItalic())
            return Font.BOLD;
        else if (textSettings.isBold() && textSettings.isItalic())
            return Font.BOLD + Font.ITALIC;
        else
            return Font.PLAIN;
    }
}
