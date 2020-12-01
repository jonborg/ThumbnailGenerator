package fighter;

import exception.OnlineImageNotFoundException;

import java.io.File;
public class DownloadFighterURL {

    static String FIGHTERS_URL = "https://www.smashbros.com/assets_v2/img/fighter/";
    static String FIGHTERS_URL_2 = "https://raw.githubusercontent.com/marcrd/smash-ultimate-assets/master/renders/";
    static String SANS_URL = "https://i.redd.it/n2tcplon8qk31.png";

    static String localFightersPath = "/assets/fighters/";

    static public String generateFighterURL(String urlName, int alt) {
        String fighterPath = System.getProperty("user.dir") + localFightersPath + urlName + "/" + alt +".png";
        File file = new File(fighterPath);
        if (!file.exists()){
            return getOnlineURL(urlName,alt);
        }
        return fighterPath;
    }

    static private  String getOnlineURL(String urlName, int alt) {
        String urlString;
        if (alt == 1) {
            urlString = FIGHTERS_URL + urlName + "/main.png";
        } else {
            urlString = FIGHTERS_URL + urlName + "/main" + alt + ".png";
        }

        if (urlName.contains("pokemon_trainer")) {
            urlString = FIGHTERS_URL_2 + "misc/pokemon-trainer-0" + alt + ".png";
        }
        if (urlName.contains("mii_brawler")) {
            urlString = FIGHTERS_URL_2 + "fighters/51/01.png";
        }
        if (urlName.contains("mii_swordfighter")) {
            urlString = FIGHTERS_URL_2 + "fighters/52/01.png";
        }
        if (urlName.contains("mii_gunner")) {
            if (alt == 1) urlString = FIGHTERS_URL_2 + "fighters/53/01.png";
            if (alt == 2) urlString = SANS_URL;
        }
        return urlString;
    }
}
