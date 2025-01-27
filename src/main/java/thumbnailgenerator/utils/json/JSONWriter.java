package thumbnailgenerator.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import thumbnailgenerator.adapter.FighterArtTypeAdapter;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.interfaces.FighterArtType;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.file.FileUtils;


public class JSONWriter {
    private static final Logger LOGGER = LogManager.getLogger(JSONWriter.class);

    private static String tournamentFile = FileUtils.getTournamentFile();
    private static String textSettingsFile = FileUtils.getTextSettingsFile();

    public static void updateTournamentsFile(List<Tournament> list){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(FighterArtType.class, new FighterArtTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter(tournamentFile)) {
            String json = gson.toJson(list);
            LOGGER.debug("Writing json to file {} -> {}", tournamentFile, json);
            writer.write(json);
        } catch (FileNotFoundException e) {
            AlertFactory.displayError("FileNotFoundException", ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }

    public static void updateTextSettingsFile(List<TextSettings> list){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(FighterArtType.class, new FighterArtTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter(textSettingsFile)) {
            String json = gson.toJson(list);
            LOGGER.debug("Writing json to file {} -> {}", textSettingsFile, json);
            writer.write(json);
        } catch (FileNotFoundException e) {
            AlertFactory.displayError("FileNotFoundException", ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }
}
