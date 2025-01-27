package thumbnailgenerator.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.GeneratedGraphic;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtType;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.StreetFighter6FighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.ui.factory.alert.AlertFactory;

public abstract class FileService<T extends GeneratedGraphic,V> {

    private static final Logger LOGGER = LogManager.getLogger(ThumbnailService.class);
    private int gameIndex = 3;

    protected abstract V getCharacterData(List<String> parameters)
            throws FighterImageSettingsNotFoundException;
    protected abstract T createEmptyGeneratedGraphic();

    public Pair<T,List<V>> readGraphicGenerationFile(InputStream inputStream){
        boolean firstLine = true;
        String line = null;
        T generatedGraphic = null;
        var list = new ArrayList<V>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                inputStream, StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                LOGGER.info("Reading line: {}", line);
                var parameters = Arrays.asList(line.split(";"));
                if (firstLine) {
                    firstLine = false;
                    generatedGraphic = initializeGeneratedGraphic(parameters);
                } else {
                    list.add(getCharacterData(parameters));
                }
            }
            return new Pair<>(generatedGraphic, list);
        } catch (IOException e) {
            LOGGER.error("Could not parse line {}.", line);
            LOGGER.catching(e);
            AlertFactory.displayError("Could not parse line: " + line);
        } catch (FighterImageSettingsNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected int getGameIndex(){
        return this.gameIndex;
    }

    protected T initializeGeneratedGraphic(List<String> parameters)
            throws IOException {
        var generatedGraphic = createEmptyGeneratedGraphic();
        for (Tournament t : TournamentUtils.getTournamentsList()) {
            if (t.getTournamentId().equals(parameters.get(0))) {
                LOGGER.info("Selected tournament {}", t.getName());
                generatedGraphic.setTournament(t);
                break;
            }
        }
        if (generatedGraphic.getTournament() == null) {
            LOGGER.error("Could not find tournament with id {}.",
                    parameters.get(0));
            AlertFactory.displayError("Could not find tournament with id '{}'",
                    parameters.get(0));
            return null;
        }
        var game = Game.valueOf(parameters.get(1).toUpperCase());
        generatedGraphic.setGame(game);
        switch (game) {
            case SSBU:
                setArtType(generatedGraphic, parameters,
                        new Pair<>(
                                SmashUltimateFighterArtType
                                        .valueOf(parameters.get(getGameIndex()).toUpperCase()),
                                SmashUltimateFighterArtType.RENDER
                        )
                );
                break;
            case SF6:
                setArtType(generatedGraphic, parameters,
                        new Pair<>(
                                StreetFighter6FighterArtType
                                        .valueOf(parameters.get(getGameIndex()).toUpperCase()),
                                StreetFighter6FighterArtType.RENDER
                        )
                );
                break;
            case ROA2:
                setArtType(generatedGraphic, parameters,
                        new Pair<>(
                                RivalsOfAether2FighterArtType
                                        .valueOf(parameters.get(getGameIndex()).toUpperCase()),
                                RivalsOfAether2FighterArtType.RENDER
                        )
                );
            default:
                throw new IOException("Incompatible Game provided");
        }
        return generatedGraphic;
    }

    private void setArtType(
            GeneratedGraphic generatedGraphic,
            List<String> parameters,
            Pair<FighterArtType, FighterArtType> fighterArtTypes
    ){
        if (parameters.size() > gameIndex
                && !parameters.get(gameIndex).isEmpty()) {
            generatedGraphic.setArtType(fighterArtTypes.getValue0());
        } else {
            generatedGraphic.setArtType(fighterArtTypes.getValue1());
        }
    }
}
