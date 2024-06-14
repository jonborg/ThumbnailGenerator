package fighter;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Names {
    private final static Map<String,String> map = new TreeMap<>();

    static{
        //64
        map.put("Mario", "mario");
        map.put("Donkey Kong", "donkey_kong");
        map.put("Link", "link");
        map.put("Samus", "samus");
        map.put("Yoshi", "yoshi");
        map.put("Kirby", "kirby");
        map.put("Fox", "fox");
        map.put("Pikachu", "pikachu");
        map.put("Luigi", "luigi");
        map.put("Ness", "ness");
        map.put("Captain Falcon", "captain_falcon");
        map.put("Jigglypuff", "jigglypuff");

        //Melee
        map.put("Peach", "peach");
        map.put("Bowser", "bowser");
        map.put("Ice Climbers", "ice_climbers");
        map.put("Sheik", "sheik");
        map.put("Zelda", "zelda");
        map.put("Dr. Mario", "dr_mario");
        map.put("Pichu", "pichu");
        map.put("Falco", "falco");
        map.put("Marth", "marth");
        map.put("Young Link", "young_link");
        map.put("Ganondorf", "ganondorf");
        map.put("Mewtwo", "mewtwo");
        map.put("Roy", "roy");
        map.put("Mr. Game & Watch", "mr_game_and_watch");

        //Brawl
        map.put("Meta Knight", "meta_knight");
        map.put("Pit", "pit");
        map.put("Zero Suit Samus", "zero_suit_samus");
        map.put("Wario", "wario");
        map.put("Snake", "snake");
        map.put("Ike", "ike");
        map.put("Pokémon Trainer", "pokemon_trainer");
        map.put("Diddy Kong", "diddy_kong");
		map.put("Lucas", "lucas");
        map.put("Sonic", "sonic");
        map.put("King Dedede", "king_dedede");
        map.put("Olimar", "olimar");
        map.put("Lucario", "lucario");
        map.put("R.O.B.", "rob");
        map.put("Toon Link", "toon_link");
        map.put("Wolf", "wolf");

        //4
        map.put("Villager", "villager");
        map.put("Mega Man", "mega_man");
        map.put("Wii Fit Trainer", "wii_fit_trainer");
        map.put("Rosalina & Luma", "rosalina_and_luma");
        map.put("Little Mac", "little_mac");
        map.put("Greninja", "greninja");
        map.put("Mii Brawler", "mii_brawler");
        map.put("Mii Swordfighter", "mii_swordfighter");
        map.put("Mii Gunner", "mii_gunner");
        map.put("Palutena", "palutena");
        map.put("Pac-Man", "pac_man");
        map.put("Robin", "robin");
        map.put("Shulk", "shulk");
        map.put("Bowser Jr.", "bowser_jr");
        map.put("Duck Hunt Duo", "duck_hunt");
        map.put("Ryu", "ryu");
        map.put("Cloud", "cloud");
        map.put("Corrin", "corrin");
        map.put("Bayonetta", "bayonetta");

        //Ultimate
        map.put("Inkling", "inkling");
        map.put("Ridley", "ridley");
        map.put("Simon", "simon");
        map.put("King K. Rool", "king_k_rool");
        map.put("Isabelle", "isabelle");
        map.put("Incineroar", "incineroar");
		map.put("Piranha Plant", "piranha_plant");
        map.put("Joker", "joker");
        map.put("Hero", "dq_hero");
        map.put("Banjo & Kazooie", "banjo_and_kazooie");
        map.put("Terry", "terry");
        map.put("Byleth", "byleth");
        map.put("Min Min", "minmin");
        map.put("Steve", "steve");
        map.put("Sephiroth", "sephiroth");
        map.put("Pyra & Mythra", "pyra");
        map.put("Kazuya", "kazuya");
        map.put("Sora", "sora");

        //echoes
        map.put("Dark Pit", "dark_pit");
        map.put("Lucina", "lucina");
        map.put("Daisy", "daisy");
        map.put("Chrom", "chrom");
        map.put("Dark Samus", "dark_samus");
        map.put("Richter", "richter");
        map.put("Ken", "ken");

        //random
        map.put("Random", "random");
    }

    public static String getValue(String key){
        return map.get(key);
    }

    public static Set<String> getKeySet(){
        return map.keySet();
    }
}
