package tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import thumbnail.text.TextSettings;

@Getter
public class Tournament implements Cloneable{

    @Expose
    @SerializedName("id")
    private String tournamentId;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("logo")
    private String image;
    @Expose
    @SerializedName("foreground")
    private String foreground;
    @Expose
    @SerializedName("background")
    private String background;
    @Expose
    @SerializedName("fighterImageSettings")
    private String fighterImageSettingsFile;

    private TextSettings textSettings;

    private static String defaultBackground= "assets/tournaments/backgrounds/default.png";

    public Tournament(String id, String name, String image, String foreground, String background, String fighterImageSettingsFile){
        this.tournamentId = id;
        this.name = name;
        this.image = image;
        this.foreground = foreground;
        this.background = background == null ? defaultBackground : background;
        this.fighterImageSettingsFile = fighterImageSettingsFile;
    }

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    public boolean updateDifferences(Object obj){
        if (obj == null || !(obj instanceof Tournament)){
            return false;
        }
        boolean equal = true;
        Tournament tournament = (Tournament) obj;
        if (!this.tournamentId.equals(tournament.getTournamentId())){
            this.tournamentId = tournament.getTournamentId();
            equal = false;
        }
        if (!this.name.equals(tournament.getName())){
            this.name = tournament.getName();
            equal = false;
        }
        if (!this.image.equals(tournament.getImage())){
            this.image = tournament.getImage();
            equal = false;
        }
        if (!this.foreground.equals(tournament.getForeground())){
            this.foreground = tournament.getForeground();
            equal = false;
        }
        if (!this.getBackground().equals(tournament.getBackground())){
            this.background = tournament.getBackground();
            equal = false;
        }
        if (!this.getFighterImageSettingsFile().equals(tournament.getFighterImageSettingsFile())){
            this.fighterImageSettingsFile = tournament.getFighterImageSettingsFile();
            equal = false;
        }
        if (!this.textSettings.updateDifferences(tournament.getTextSettings())){
            this.textSettings = tournament.getTextSettings();
            equal = false;
        }
        return equal;
    }

    public String getBackground() {
        return background == null ? defaultBackground : background;
    }

    public void setTextSettings(TextSettings textSettings) {
        this.textSettings = textSettings;
    }
}
