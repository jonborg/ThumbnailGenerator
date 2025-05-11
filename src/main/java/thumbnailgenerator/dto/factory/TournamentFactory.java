package thumbnailgenerator.dto.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thumbnailgenerator.dto.FighterArtSettings;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.dto.json.read.FighterArtSettingsRead;
import thumbnailgenerator.dto.json.read.FileThumbnailSettingsRead;
import thumbnailgenerator.dto.json.read.FileTop8SettingsRead;
import thumbnailgenerator.dto.json.read.TournamentRead;
import thumbnailgenerator.service.GameEnumService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TournamentFactory {

    @Autowired
    private GameEnumService gameEnumService;

    public Tournament createTournament(TournamentRead tournamentRead){
        return new Tournament(
                tournamentRead.getId(),
                tournamentRead.getName(),
                tournamentRead.getImage(),
                tournamentRead.getThumbnailSettings()
                        .stream()
                        .map(this::createFileThumbnailSettings)
                        .collect(Collectors.toList()),
                tournamentRead.getTop8Settings()
                        .stream()
                        .map(this::createFileTop8Settings)
                        .collect(Collectors.toList())
        );
    }

    private FileThumbnailSettings createFileThumbnailSettings(
            FileThumbnailSettingsRead fileThumbnailSettingsRead){
        return new FileThumbnailSettings(
                Game.valueOf(fileThumbnailSettingsRead.getGame()),
                fileThumbnailSettingsRead.getForeground(),
                fileThumbnailSettingsRead.getBackground(),
                createThumbnailFighterArtSettings(fileThumbnailSettingsRead.getArtTypeDir(),Game.valueOf(fileThumbnailSettingsRead.getGame())),
                new TextSettings((String) null)
        );
    }

    private FileTop8Settings createFileTop8Settings(FileTop8SettingsRead fileTop8SettingsRead){
        return new FileTop8Settings(
                Game.valueOf(fileTop8SettingsRead.getGame()),
                fileTop8SettingsRead.getForeground(),
                fileTop8SettingsRead.getBackground(),
                createTop8FighterArtSettings(fileTop8SettingsRead.getArtTypeDir(), Game.valueOf(fileTop8SettingsRead.getGame())),
                fileTop8SettingsRead.getSlotSettingsFile()
        );
    }

    public List<FighterArtSettings> createThumbnailFighterArtSettings(List<FighterArtSettingsRead> fighterArtSettingsReadList, Game game){
        return fighterArtSettingsReadList.stream()
                .map(art -> {
                    var artType = gameEnumService.getArtTypeEnum(game, art.getArtType());
                    var settingsPath = art.getFighterImageSettingsPath() == null ?
                            gameEnumService.getDefaultFighterArtTypeSettingsFile(game, artType) :
                            art.getFighterImageSettingsPath();
                    return FighterArtSettings.builder()
                            .artType(artType)
                            .fighterImageSettingsPath(settingsPath)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<FighterArtSettings> createTop8FighterArtSettings(List<FighterArtSettingsRead> fighterArtSettingsReadList, Game game){
        return fighterArtSettingsReadList.stream()
                .map(art -> {
                    var artType = gameEnumService.getArtTypeEnum(game, art.getArtType());
                    return FighterArtSettings.builder()
                            .artType(artType)
                            .fighterImageSettingsPath(art.getFighterImageSettingsPath())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
