package thumbnailgenerator.utils.mapper;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class StreetFighter6Mapper {

    private final static Map<String,String> map = new TreeMap<>();

    static{
        map.put("Ryu", "ryu");
        map.put("Luke", "luke");
        map.put("Jamie", "jamie");
        map.put("Chun-Li", "chunli");
        map.put("Guile", "guile");
        map.put("Kimberly", "kimberly");
        map.put("Juri", "juri");
        map.put("Ken", "ken");
        map.put("Blanka", "blanka");
        map.put("Dhalsim", "dhalsim");
        map.put("E.Honda", "ehonda");
        map.put("Dee Jay", "deejay");
        map.put("Manon", "manon");
        map.put("Marisa", "marisa");
        map.put("JP", "jp");
        map.put("Zangief", "zangief");
        map.put("Lily", "lily");
        map.put("Cammy", "cammy");
        map.put("Rashid", "rashid");
        map.put("A.K.I.", "aki");
        map.put("Ed", "ed");
        map.put("Akuma", "akuma");
    }

    public static String getValue(String key){
        return map.get(key);
    }

    public static Set<String> getKeySet(){
        return map.keySet();
    }
}
