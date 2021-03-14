package file.json;


import com.google.gson.*;
import file.FileUtils;
import org.codehaus.plexus.util.ExceptionUtils;
import thumbnail.text.TextSettings;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;
import tournament.Tournament;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONWriter {
    private static String tournamentFile = FileUtils.getTournamentFile();
    private static String textSettingsFile = FileUtils.getTextSettingsFile();


    public static void updateTournamentsFile(List<Tournament> list){
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter(tournamentFile)) {
            String json = gson.toJson(list);
            System.out.println(json);
            writer.write(json);
        } catch (FileNotFoundException e) {
            AlertFactory.displayError("FileNotFoundException", ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }

    public static void updateTextSettingsFile(List<TextSettings> list){
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try (FileWriter writer = new FileWriter(textSettingsFile)) {
            String json = gson.toJson(list);
            System.out.println(json);
            writer.write(json);
        } catch (FileNotFoundException e) {
            AlertFactory.displayError("FileNotFoundException", ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        }
    }
}
