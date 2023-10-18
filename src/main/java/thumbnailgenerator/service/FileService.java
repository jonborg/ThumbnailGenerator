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
import thumbnailgenerator.dto.GeneratedGraphic;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.ui.factory.alert.AlertFactory;

public abstract class FileService<T extends GeneratedGraphic,V> {

    private static final Logger LOGGER = LogManager.getLogger(ThumbnailService.class);

    protected abstract T initializeGeneratedGraphic(List<String> parameters);

    protected abstract V getCharacterData(List<String> parameters)
            throws FighterImageSettingsNotFoundException;

    public Pair<T,List<V>> readGraphicGenerationFile(InputStream inputStream){
        boolean firstLine = true;
        String line = null;
        T generatedGraphic = null;
        var characterList = new ArrayList<V>();

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
                    characterList.add(getCharacterData(parameters));
                }
            }
            return new Pair<>(generatedGraphic, characterList);
        } catch (IOException e) {
            LOGGER.error("Could not parse line {}.", line);
            LOGGER.catching(e);
            AlertFactory.displayError("Could not parse line: " + line);
        } catch (FighterImageSettingsNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
