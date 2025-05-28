package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.dto.Round;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.service.json.JSONReaderService;

@Service
public class ThumbnailFileService extends FileService<Thumbnail, Round> {

    private int gameIndex = 3;
    private @Autowired SmashUltimateCharacterService smashUltimateCharacterService;
    private @Autowired JSONReaderService jsonReaderService;
    private @Autowired TournamentService tournamentService;

    public List<Thumbnail> getListThumbnailsFromFile(InputStream inputStream, Boolean saveLocally)
            throws FighterImageSettingsNotFoundException {
        var tuple = readGraphicGenerationFile(inputStream);
        var rootThumbnail = tuple.getValue0();
        var roundList = tuple.getValue1();
        var imageSettings = (ImageSettings) jsonReaderService.getJSONObjectFromFile(
                tournamentService.getTournamentThumbnailSettingsOrDefault(rootThumbnail.getTournament(), rootThumbnail.getGame())
                        .getFighterImageSettingsFile(rootThumbnail.getArtType()),
                new TypeToken<ImageSettings>() {}.getType());
        var thumbnailList = new ArrayList<Thumbnail>();
        for (Round round : roundList){
            setPlayerFlip(round.getPlayers(), imageSettings, rootThumbnail.getGame());
            var thumbnail = Thumbnail.builder()
                                     .tournament(rootThumbnail.getTournament())
                                     .game(rootThumbnail.getGame())
                                     .imageSettings(imageSettings)
                                     .locally(saveLocally)
                                     .round(round.getRoundName())
                                     .date(rootThumbnail.getDate())
                                     .players(round.getPlayers())
                                     .artType(rootThumbnail.getArtType())
                                     .build();
            thumbnailList.add(thumbnail);
        }
        return thumbnailList;
    }

    @Override
    protected Thumbnail initializeGeneratedGraphic(List<String> parameters)
            throws IOException {
        var thumbnail = super.initializeGeneratedGraphic(parameters);
        thumbnail.setDate(parameters.get(2));
        return thumbnail;
    }

    @Override
    protected Thumbnail createEmptyGeneratedGraphic() {
        return Thumbnail.builder().build();
    }

    @Override
    protected int getGameIndex(){
        return this.gameIndex;
    }

    @Override
    protected Round getCharacterData(List<String> parameters) {
        var playerList = new ArrayList<Player>();

        playerList.add(preparePlayerData(parameters, 0));
        playerList.add(preparePlayerData(parameters, 1));

        var roundName = parameters.get(6);
        return new Round(playerList, roundName);
    }

    private Player preparePlayerData(List<String> parameters, int playerIndex) {
        var charactersList = Arrays.asList(parameters.get(2 + playerIndex).split(","));
        var altList = Arrays.stream(parameters.get(4 + playerIndex).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        var playerCharactersList = new ArrayList<Fighter>();

        for (int i = 0; i < charactersList.size(); i++){
            var alt = 1;
            if (i < altList.size()){
                alt = altList.get(i);
            }
            playerCharactersList.add(new Fighter(charactersList.get(i), charactersList.get(i), alt, false));
        }
        return new Player(parameters.get(0 + playerIndex), playerCharactersList);
    }

    private void setPlayerFlip(List<Player> players, ImageSettings imageSettings, Game game)
            throws FighterImageSettingsNotFoundException {

        players.get(0).setFighterFlip(0, readFlipFile(players.get(0).getFighter(0), imageSettings, game));
        if (imageSettings.isMirrorPlayer2()) {
            players.get(1).setFighterFlip(0, !readFlipFile(players.get(1).getFighter(0), imageSettings, game));
        } else {
            players.get(1).setFighterFlip(0, readFlipFile(players.get(1).getFighter(0), imageSettings, game));
        }
    }

    private boolean readFlipFile(Fighter fighter, ImageSettings imageSettings, Game game) throws
            FighterImageSettingsNotFoundException {
        boolean result;
        String urlNameOriginal = fighter.getUrlName();
        if (Game.SSBU.equals(game)) {
            smashUltimateCharacterService.convertToAlternateRender(fighter);
        }
        result = imageSettings.findFighterImageSettings(fighter.getUrlName()).isFlip();
        fighter.setUrlName(urlNameOriginal);
        return result;
    }
}
