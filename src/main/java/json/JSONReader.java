package json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import org.codehaus.plexus.util.ExceptionUtils;
import ui.factory.alert.AlertFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JSONReader {
    static AlertFactory alertFactory = new AlertFactory();

    public static <T> List<T> getJSONArray(String jsonFile, Type type){
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(jsonFile))
        {
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            alertFactory.displayError("FileNotFoundException", ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            alertFactory.displayError("IOException", ExceptionUtils.getStackTrace(e));
        } catch (JsonSyntaxException e){
            alertFactory.displayError("JsonSyntaxException", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
