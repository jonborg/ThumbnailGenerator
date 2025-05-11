package thumbnailgenerator.dto;

import thumbnailgenerator.dto.json.read.TextSettingsRead;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TextSettings {
    private String tournamentId;
    private String font;
    private boolean bold;
    private boolean italic;
    private boolean shadow;
    private float contour;
    private int sizeTop;
    private int sizeBottom;
    private float angleTop;
    private float angleBottom;
    private int[] downOffsetTop;
    private int[] downOffsetBottom;

    public TextSettings(String tournamentId, String font, boolean bold, boolean italic, boolean shadow, float contour, int sizeTop, int sizeBottom, float angleTop, float angleBottom, int[] downOffsetTop, int[] downOffsetBottom) {
        this.tournamentId = tournamentId;
        this.font = font;
        this.bold = bold;
        this.italic = italic;
        this.shadow = shadow;
        this.contour = contour;
        this.sizeTop = sizeTop;
        this.sizeBottom = sizeBottom;
        this.angleTop = angleTop;
        this.angleBottom = angleBottom;
        this.downOffsetTop = downOffsetTop;
        this.downOffsetBottom = downOffsetBottom;
    }

    public TextSettings(TextSettings textSettings, String suffix){
        this(
                textSettings.getTournamentId() + suffix,
                textSettings.getFont(),
                textSettings.isBold(),
                textSettings.isItalic(),
                textSettings.isShadow(),
                textSettings.getContour(),
                textSettings.getSizeTop(),
                textSettings.getSizeBottom(),
                textSettings.getAngleTop(),
                textSettings.getAngleBottom(),
                textSettings.getDownOffsetTop(),
                textSettings.getDownOffsetBottom()
        );
    }

    public TextSettings(TextSettingsRead textSettingsRead){
        this(
                textSettingsRead.getId(),
                textSettingsRead.getFont(),
                textSettingsRead.isBold(),
                textSettingsRead.isItalic(),
                textSettingsRead.isShadow(),
                textSettingsRead.getContour(),
                textSettingsRead.getSizeTop(),
                textSettingsRead.getSizeBottom(),
                textSettingsRead.getAngleTop(),
                textSettingsRead.getAngleBottom(),
                textSettingsRead.getDownOffsetTop(),
                textSettingsRead.getDownOffsetBottom()
        );
    }

    public TextSettings(String tournamentId){
        this(tournamentId, "BebasNeue-Regular", false, false, false,
                0.0f, 90, 75, 0, 0,
                new int[]{0, 0}, new int[]{0, 0});
    }


}
