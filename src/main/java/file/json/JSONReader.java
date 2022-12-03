package file.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.codehaus.plexus.util.ExceptionUtils;
import ui.factory.alert.AlertFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JSONReader {

    public static <T> List<T> getJSONArrayFromFile(String jsonFile, Type type){
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
