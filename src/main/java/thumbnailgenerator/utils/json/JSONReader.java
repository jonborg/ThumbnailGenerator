package thumbnailgenerator.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import org.codehaus.plexus.util.ExceptionUtils;
import thumbnailgenerator.adapter.FileThumbnailSettingsTypeAdapter;
import thumbnailgenerator.adapter.FileTop8SettingsTypeAdapter;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.ui.factory.alert.AlertFactory;

public class JSONReader {

    public static <T> List<T> getJSONArrayFromFile(String jsonFile, Type type){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FileThumbnailSettings.class, new FileThumbnailSettingsTypeAdapter())
                .registerTypeAdapter(FileTop8Settings.class, new FileTop8SettingsTypeAdapter())
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

    public static <T> Object getJSONObjectFromFile(String jsonFile, Type type){
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

    public static <T> Object getJSONObject(String jsonText, Type type){
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonText, type);
        } catch (JsonSyntaxException e){
            AlertFactory.displayError("JsonSyntaxException", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
