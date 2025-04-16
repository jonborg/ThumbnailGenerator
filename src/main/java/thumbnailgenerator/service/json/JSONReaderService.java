package thumbnailgenerator.service.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.json.read.TextSettingsRead;
import thumbnailgenerator.dto.json.read.TournamentRead;
import thumbnailgenerator.ui.factory.alert.AlertFactory;

@Service
public class JSONReaderService {

    @Value("${settings.tournament.file.path}")
    private String tournamentFile;
    @Value("${settings.text.file.path}")
    private String textSettingsFile;

    public <T> List<T> getJSONArrayFromFile(String jsonFile, Type type){
        Gson gson = new GsonBuilder()
                .create();

        try (FileReader reader = new FileReader(jsonFile))
        {
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            AlertFactory.displayError("FileNotFoundException", ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        } catch (JsonSyntaxException e){
            AlertFactory.displayError("JsonSyntaxException", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public <T> Object getJSONObjectFromFile(String jsonFile, Type type){
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(jsonFile))
        {
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            AlertFactory.displayError("FileNotFoundException", ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            AlertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        } catch (JsonSyntaxException e){
            AlertFactory.displayError("JsonSyntaxException", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public <T> Object getJSONObject(String jsonText, Type type){
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonText, type);
        } catch (JsonSyntaxException e){
            AlertFactory.displayError("JsonSyntaxException", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public TextSettings loadTextSettings(String tournamentId) {

        List<TextSettingsRead> textSettingsReadList =
                        getJSONArrayFromFile(textSettingsFile, new TypeToken<ArrayList<TextSettingsRead>>() {}.getType());
        List<TextSettings> textSettingsList = textSettingsReadList
                .stream()
                .map(TextSettings::new)
                .collect(Collectors.toList());
        for (TextSettings textSettings : textSettingsList) {
            if (textSettings.getTournamentId().equals(tournamentId)) {
                return textSettings;
            }
        }
        return null;
    }

    public List<TournamentRead> loadTournament(){
        return getJSONArrayFromFile(tournamentFile, new TypeToken<ArrayList<TournamentRead>>(){}.getType());
    }
}
