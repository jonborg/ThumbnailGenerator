package json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ui.factory.alert.AlertFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONProcessor {
    static AlertFactory alertFactory = new AlertFactory();

    public static JSONArray getJSONArray(String jsonFile){
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(jsonFile))
        {
            return (JSONArray) jsonParser.parse(reader);
        } catch (FileNotFoundException e) {
            alertFactory.displayError("FileNotFoundException", e.getStackTrace().toString());
        } catch (IOException e) {
            alertFactory.displayError("IOException", e.getStackTrace().toString());
        } catch (ParseException e) {
            alertFactory.displayError("ParseException", e.getStackTrace().toString());
        }
        return null;
    }

    public static int toInt(JSONObject object, String key){
        Long value = (Long) object.get(key);
        return object.containsKey(key) ? value.intValue() : 0;
    }

    public static int[] toIntArray(JSONObject object, String key){
        if (!object.containsKey(key)){
            return new int[] {0,0};
        }
        JSONArray array = (JSONArray) object.get(key);
        Object[] arrayString =  array.toArray();

        int[] arrayInt = new int[arrayString.length];
        for (int i = 0; i< arrayString.length; i++){
            Long aux = (Long) arrayString[i];
            arrayInt[i] = aux.intValue();
        }
        return arrayInt;
    }

    public static float toFloat(JSONObject object, String key){
        Long value = (Long) object.get(key);
        return object.containsKey(key) ? value : 0f;
    }

    public static boolean toBoolean(JSONObject object, String key){
        Boolean value = (Boolean) object.get(key);
        return object.containsKey(key) ? value.booleanValue() : false;
    }

}
