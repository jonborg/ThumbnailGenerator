package json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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
            alertFactory.displayError("FileNotFoundException", e.getStackTrace().toString());
        } catch (IOException e) {
            alertFactory.displayError("IOException", e.getStackTrace().toString());
        }
        return null;
    }
}
