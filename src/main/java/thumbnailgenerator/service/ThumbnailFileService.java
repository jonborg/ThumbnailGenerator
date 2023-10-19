package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.dto.Round;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.utils.json.JSONReader;

@Service
public class ThumbnailFileService extends FileService<Thumbnail, Round> {

    private static final Logger LOGGER = LogManager.getLogger(ThumbnailFileService.class);
    private @Autowired SmashUltimateCharacterService smashUltimateCharacterService;

    public List<Thumbnail> getListThumbnailsFromFile(InputStream inputStream, Boolean saveLocally)
            throws FighterImageSettingsNotFoundException {
        var tuple = readGraphicGenerationFile(inputStream);
        var rootThumbnail = tuple.getValue0();
        var roundList = tuple.getValue1();
        var imageSettings = (ImageSettings) JSONReader.getJSONArrayFromFile(
                rootThumbnail.getTournament().getThumbnailSettings()
                        .getFighterImageSettingsFile(rootThumbnail.getArtType()),
                new TypeToken<ArrayList<ImageSettings>>() {}.getType())
                .get(0);
        var thumbnailList = new ArrayList<Thumbnail>();
        for (Round round : roundList){
            setPlayerFlip(round.getPlayers(), imageSettings);
            var thumbnail = Thumbnail.builder()
                                     .tournament(rootThumbnail.getTournament())
                                     .game(Game.SMASH_ULTIMATE)
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
    protected Thumbnail initializeGeneratedGraphic(List<String> parameters) {
        var thumbnail = super.initializeGeneratedGraphic(parameters);
        thumbnail.setDate(parameters.get(2));
        if (Game.SMASH_ULTIMATE.equals(thumbnail.getGame())) {
            if (parameters.size() > 3
                    && !parameters.get(3).isEmpty()) {
                thumbnail.setArtType(SmashUltimateFighterArtType
                        .valueOf(parameters.get(3).toUpperCase()));
            } else {
                thumbnail.setArtType(SmashUltimateFighterArtType.RENDER);
            }
        }
        return thumbnail;
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

    private void setPlayerFlip(List<Player> players, ImageSettings imageSettings)
            throws FighterImageSettingsNotFoundException {

        players.get(0).setFighterFlip(0, readFlipFile(players.get(0).getFighter(0), imageSettings));
        if (imageSettings.isMirrorPlayer2()) {
            players.get(1).setFighterFlip(0, !readFlipFile(players.get(1).getFighter(0), imageSettings));
        } else {
            players.get(1).setFighterFlip(0, readFlipFile(players.get(1).getFighter(0), imageSettings));
        }
    }

    private boolean readFlipFile(Fighter fighter, ImageSettings imageSettings) throws
            FighterImageSettingsNotFoundException {
        boolean result;
        String urlNameOriginal = fighter.getUrlName();
        smashUltimateCharacterService.convertToAlternateRender(fighter);

        result = imageSettings.findFighterImageSettings(fighter.getUrlName()).isFlip();
        fighter.setUrlName(urlNameOriginal);
        return result;
    }
}
