package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.var;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.dto.Round;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.utils.json.JSONReader;

@Service
public class ThumbnailFileService extends FileService<Thumbnail, Round> {

    private int gameIndex = 3;
    private @Autowired SmashUltimateCharacterService smashUltimateCharacterService;

    public List<Thumbnail> getListThumbnailsFromFile(InputStream inputStream, Boolean saveLocally)
            throws FighterImageSettingsNotFoundException {
        var tuple = readGraphicGenerationFile(inputStream);
        var rootThumbnail = tuple.getValue0();
        var roundList = tuple.getValue1();
        var imageSettings = (ImageSettings) JSONReader.getJSONArrayFromFile(
                rootThumbnail.getFileThumbnailSettings()
                        .getFighterImageSettingsFile(rootThumbnail.getArtType()),
                new TypeToken<ArrayList<ImageSettings>>() {}.getType())
                .get(0);
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
        var player1 = new Player(parameters.get(0), parameters.get(2), parameters.get(2), Integer.parseInt(parameters.get(4)), false);
        var player2 = new Player(parameters.get(1), parameters.get(3), parameters.get(3), Integer.parseInt(parameters.get(5)), false);

        playerList.add(player1);
        playerList.add(player2);

        var roundName = parameters.get(6);
        return new Round(playerList, roundName);
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
