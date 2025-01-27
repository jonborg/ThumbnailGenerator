package thumbnailgenerator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import thumbnailgenerator.enums.RivalsOfAether2Enum;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtType;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.StreetFighter6FighterArtType;
import thumbnailgenerator.enums.Tekken8FighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;

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
    @SerializedName("thumbnailSettings")
    private List<FileThumbnailSettings> thumbnailSettings;
    @Expose
    @SerializedName("top8Settings")
    private List<FileTop8Settings> top8Settings;

    public Tournament(String id, String name, String image,
                      List<FileThumbnailSettings> thumbnailSettings,
                      List<FileTop8Settings> top8Settings){
        this.tournamentId = id;
        this.name = name;
        this.image = image;
        this.thumbnailSettings = thumbnailSettings;
        this.top8Settings = top8Settings;
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
        if (!Objects.equals(this.thumbnailSettings,
                tournament.getThumbnailSettings())){
            this.thumbnailSettings = tournament.getThumbnailSettings();
            equal = false;
        }
        if (!Objects.equals(this.top8Settings,
                tournament.getTop8Settings())){
            this.top8Settings = tournament.getTop8Settings();
            equal = false;
        }
        return equal;
    }

    public FileThumbnailSettings getThumbnailSettingsByGame(Game game){
        return thumbnailSettings.stream().filter(ts -> game.equals(ts.getGame())).findFirst()
                .orElse(new FileThumbnailSettings(game, null, null, generateArtTypeSettings(game),
                        new TextSettings(null)));
    }

    public FileTop8Settings getTop8SettingsByGame(Game game){
        return top8Settings.stream().filter(ts -> game.equals(ts.getGame())).findFirst()
                .orElse(new FileTop8Settings(game, null, null, generateArtTypeSettings(game), null));
    }

    private List<FighterArtSettings> generateArtTypeSettings(Game game){
        List<FighterArtSettings> artTypeSettings = new ArrayList<>();
        switch (game) {
            case SSBU:
                artTypeSettings.add(
                        new FighterArtSettings(SmashUltimateFighterArtType.RENDER,
                                SmashUltimateFighterArtType.RENDER.getDefaultFighterImageSettingsFile())
                );
                artTypeSettings.add(
                        new FighterArtSettings(SmashUltimateFighterArtType.MURAL,
                                SmashUltimateFighterArtType.MURAL.getDefaultFighterImageSettingsFile())
                );
                break;
            case ROA2:
                artTypeSettings.add(
                        new FighterArtSettings(RivalsOfAether2FighterArtType.RENDER,
                                RivalsOfAether2FighterArtType.RENDER.getDefaultFighterImageSettingsFile())
                );
                break;
            case SF6:
                artTypeSettings.add(
                        new FighterArtSettings(StreetFighter6FighterArtType.RENDER,
                                StreetFighter6FighterArtType.RENDER.getDefaultFighterImageSettingsFile())
                );
                break;
            case TEKKEN8:
                artTypeSettings.add(
                        new FighterArtSettings(Tekken8FighterArtType.RENDER,
                                Tekken8FighterArtType.RENDER.getDefaultFighterImageSettingsFile())
                );
                break;
        }
        return artTypeSettings;
    }
}
