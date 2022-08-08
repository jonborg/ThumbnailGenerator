package tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fighter.FighterArtSettingsFile;
import fighter.FighterArtType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import thumbnail.text.TextSettings;

@Getter
@ToString
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
    @SerializedName("artSettings")
    private List<FighterArtSettingsFile> artTypeDir;
    @Expose(serialize = false)
    @SerializedName("fighterImageSettings")
    private String deprecatedImageSettings;

    private TextSettings textSettings;

    private static String defaultBackground= "assets/tournaments/backgrounds/default.png";

    private static String defaultRenderImageSettingsFile= "settings/thumbnails/images/default.json";

    private static String defaultMuralImageSettingsFile= "settings/thumbnails/images/defaultMural.json";

    public Tournament(String id, String name, String image,
                      String foreground, String background,
                      List<FighterArtSettingsFile> artTypeDir, String...additional){
        this.tournamentId = id;
        this.name = name;
        this.image = image;
        this.foreground = foreground;
        this.background = background == null ? defaultBackground : background;
        this.artTypeDir = artTypeDir;
        this.artTypeDir.forEach(dir -> {
            if(dir.getFighterImageSettingsPath() == null
                    || dir.getFighterImageSettingsPath().isEmpty()) {
                switch (dir.getArtType()) {
                    case MURAL:
                        dir.setFighterImageSettingsPath(
                                defaultMuralImageSettingsFile);
                        break;
                    case RENDER:
                    default:
                        dir.setFighterImageSettingsPath(
                                defaultRenderImageSettingsFile);
                        break;
                }
            }
        });
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
        if (this.getArtTypeDir() == null
                || !this.getArtTypeDir().equals(tournament.getArtTypeDir())){
            this.artTypeDir = tournament.getArtTypeDir();
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

    public String getDeprecatedImageSettings(){
        return deprecatedImageSettings;
    }

    public String getFighterImageSettingsFile(FighterArtType artType){
        if (this.artTypeDir == null){
            switch (artType){
                case MURAL:
                    return defaultMuralImageSettingsFile;
                case RENDER:
                default:
                    return deprecatedImageSettings == null || deprecatedImageSettings
                            .isEmpty() ?
                            defaultRenderImageSettingsFile :
                            deprecatedImageSettings;
            }
        }
        return this.artTypeDir
                    .stream()
                    .filter(dir -> artType.equals(dir.getArtType()))
                    .findFirst()
                    .get()
                    .getFighterImageSettingsPath();
    }
}
