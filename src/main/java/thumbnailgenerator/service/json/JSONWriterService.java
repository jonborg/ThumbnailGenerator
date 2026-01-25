package thumbnailgenerator.service.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Settings;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.dto.json.write.FileThumbnailSettingsWrite;
import thumbnailgenerator.dto.json.write.FileTop8SettingsWrite;
import thumbnailgenerator.dto.json.write.TextSettingsWrite;
import thumbnailgenerator.dto.json.write.TournamentListWrite;
import thumbnailgenerator.dto.json.write.TournamentWrite;
import thumbnailgenerator.ui.factory.alert.AlertFactory;

@Service
public class JSONWriterService {
    private static final Logger LOGGER = LogManager.getLogger(JSONWriterService.class);
    @Value("${settings.tournament.files.path}")
    private String tournamentFilePath;
    @Value("${settings.tournament.file.path}")
    private String tournamentListFile;
    @Value("${settings.tournament.file.split.suffix}")
    private String tournamentFileSuffix;

    @Value("${settings.text.file.path}")
    private String textSettingsFile;

    //update main tournament list file
    public void updateTournamentsFile(List<Tournament> list){
        List<TournamentWrite> tournamentWriteList = list.stream().map(TournamentWrite::new).collect(Collectors.toList());
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter(tournamentListFile)) {
            String json = gson.toJson(tournamentWriteList);
            LOGGER.debug("Writing json to file {} -> {}", tournamentListFile, json);
            writer.write(json);
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }

    //update main tournament list file
    public void updateTournamentListFile(TournamentListWrite output){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter(tournamentListFile)) {
            String json = gson.toJson(output);
            LOGGER.debug("Writing json to file {} -> {}", tournamentListFile, json);
            writer.write(json);
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }

    //update specific tournament data file
    public void updateTournamentFiles(Tournament t) {
        t.getThumbnailSettings()
                .forEach(s ->
                        updateTournamentFile(t, s, "thumbnail_settings.json"));
        t.getTop8Settings()
                .forEach(s ->
                        updateTournamentFile(t, s, "top8_settings.json"));
    }

    public void updateTournamentFile(Tournament t, Settings s, String filename){
        String tournamentSettingsFile = tournamentFilePath + t.getTournamentId()
                + "/" + s.getGame()
                + "/" + filename;

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try {
            Path filePath = Paths.get(tournamentSettingsFile);
            Path parentDir = filePath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            try (FileWriter writer = new FileWriter(tournamentSettingsFile)) {
                Object output = s instanceof FileThumbnailSettings ?
                        new FileThumbnailSettingsWrite((FileThumbnailSettings) s) :
                        new FileTop8SettingsWrite((FileTop8Settings) s);
                String json = gson.toJson(output);
                LOGGER.debug("Writing json to file {} -> {}", tournamentSettingsFile, json);
                writer.write(json);
            }
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }

    public void updateTextSettingsFile(List<TextSettings> list){
        List<TextSettingsWrite> textSettingsWrite = list.stream().map(TextSettingsWrite::new).collect(Collectors.toList());
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter(textSettingsFile)) {
            String json = gson.toJson(textSettingsWrite);
            LOGGER.debug("Writing json to file {} -> {}", textSettingsFile, json);
            writer.write(json);
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }

    public void updateThumbnailImageSettings(ImageSettings imageSettings, String imageSettingsFile){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter(imageSettingsFile)) {
            String json = gson.toJson(imageSettings);
            LOGGER.debug("Writing json to file {} -> {}", imageSettingsFile, json);
            writer.write(json);
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }
}
