package thumbnail.text;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import file.json.JSONReader;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import tournament.Tournament;

@Getter
@Setter
public class TextSettings {
    @Expose
    @SerializedName("id")
    private String tournamentId;
    @Expose
    @SerializedName("font")
    private String font;
    @Expose
    @SerializedName("bold")
    private boolean bold;
    @Expose
    @SerializedName("italic")
    private boolean italic;
    @Expose
    @SerializedName("shadow")
    private boolean shadow;
    @Expose
    @SerializedName("contour")
    private float contour;
    @Expose
    @SerializedName("sizeTop")
    private int sizeTop;
    @Expose
    @SerializedName("sizeBottom")
    private int sizeBottom;
    @Expose
    @SerializedName("angleTop")
    private float angleTop;
    @Expose
    @SerializedName("angleBottom")
    private float angleBottom;
    @Expose
    @SerializedName("downOffsetTop")
    private int[] downOffsetTop;
    @Expose
    @SerializedName("downOffsetBottom")
    private int[] downOffsetBottom;

    private static String textSettingsFile = "settings/thumbnails/text/text.json";

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

    public static TextSettings loadTextSettings(String tournamentId) {

        List<TextSettings> textSettingsList =
                JSONReader.getJSONArray(textSettingsFile, new TypeToken<ArrayList<TextSettings>>() {
                }.getType());
        for (TextSettings textSettings : textSettingsList) {
            if (textSettings.getTournamentId().equals(tournamentId)) {
                return textSettings;
            }
        }
        return null;
    }

    public static List<TextSettings> getAllTextSettings(List<Tournament> tournamentList) {
        List<TextSettings> textSettingsList = new ArrayList<>();
        tournamentList.forEach(tournament -> textSettingsList.add(tournament.getTextSettings()));
        return textSettingsList;
    }

    public static List<TextSettings> loadAllTextSettings() {
        return JSONReader.getJSONArray(textSettingsFile, new TypeToken<ArrayList<Tournament>>(){}.getType());
    }

    public boolean updateDifferences(Object obj){
        if (obj == null || !(obj instanceof TextSettings)){
            return false;
        }
        boolean equal = true;
        TextSettings ts = (TextSettings) obj;
        if (!this.tournamentId.equals(ts.getTournamentId())){
            this.tournamentId = ts.getTournamentId();
            equal = false;
        }
        if (!this.font.equals(ts.getFont())){
            this.font = ts.getFont();
            equal = false;
        }
        if (this.bold != ts.isBold()){
            this.bold = ts.isBold();
            equal = false;
        }
        if (this.italic != ts.isItalic()){
            this.italic = ts.isItalic();
            equal = false;
        }
        if (this.shadow != ts.isShadow()){
            this.shadow = ts.isShadow();
            equal = false;
        }
        if (this.contour != ts.getContour()){
            this.contour = ts.getContour();
            equal = false;
        }
        if (this.sizeTop != ts.getSizeTop()){
            this.sizeTop = ts.getSizeTop();
            equal = false;
        }
        if (this.sizeBottom != ts.getSizeBottom()){
            this.sizeBottom = ts.getSizeBottom();
            equal = false;
        }
        if (this.angleTop != ts.getAngleTop()){
            this.angleTop = ts.getAngleTop();
            equal = false;
        }
        if (this.angleBottom != ts.getAngleBottom()){
            this.angleBottom = ts.getAngleBottom();
            equal = false;
        }
        if (this.downOffsetTop[0] != ts.getDownOffsetTop()[0]){
            this.downOffsetTop[0] = ts.getDownOffsetTop()[0];
            equal = false;
        }
        if (this.downOffsetTop[1] != ts.getDownOffsetTop()[1]){
            this.downOffsetTop[1] = ts.getDownOffsetTop()[1];
            equal = false;
        }
        if (this.downOffsetBottom[0] != ts.getDownOffsetBottom()[0]){
            this.downOffsetBottom[0] = ts.getDownOffsetBottom()[0];
            equal = false;
        }
        if (this.downOffsetBottom[1] != ts.getDownOffsetBottom()[1]){
            this.downOffsetBottom[1] = ts.getDownOffsetBottom()[1];
            equal = false;
        }
        return equal;
    }
}
