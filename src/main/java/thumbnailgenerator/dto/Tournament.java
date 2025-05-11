package thumbnailgenerator.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.ToString;
import thumbnailgenerator.dto.json.read.TournamentRead;
import thumbnailgenerator.enums.FatalFuryCotwFighterArtTypeEnum;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtTypeEnum;
import thumbnailgenerator.enums.SmashUltimateFighterArtTypeEnum;
import thumbnailgenerator.enums.StreetFighter6FighterArtTypeEnum;
import thumbnailgenerator.enums.Tekken8FighterArtTypeEnum;

@Getter
@ToString
public class Tournament implements Cloneable{
    private String tournamentId;
    private String name;
    private String image;
    private List<FileThumbnailSettings> thumbnailSettings;
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

    public Tournament(Tournament tournament, String suffix){
        this(
                tournament.getTournamentId() + suffix,
                tournament.getName() + suffix,
                tournament.getImage(),
                tournament.getThumbnailSettings()
                        .stream()
                        .map(t -> new FileThumbnailSettings(t, suffix))
                        .collect(Collectors.toList()),
                tournament.getTop8Settings().stream().map(FileTop8Settings::new).collect(Collectors.toList())
        );
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
        return thumbnailSettings.stream().filter(ts -> game.equals(ts.getGame()))
                .findFirst()
                .orElse(null);
    }

    public FileTop8Settings getTop8SettingsByGame(Game game){
        return top8Settings.stream().filter(ts -> game.equals(ts.getGame()))
                .findFirst()
                .orElse(null);
    }
}
