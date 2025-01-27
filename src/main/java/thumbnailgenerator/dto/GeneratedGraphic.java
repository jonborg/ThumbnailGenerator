package thumbnailgenerator.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.var;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
@Setter
public class GeneratedGraphic {
    Tournament tournament;
    Game game;
    boolean locally;
    List<Player> players;
    FighterArtType artType;

    public static List<Player> createPlayerList(Player... players){
        var result = new ArrayList<Player>();
        for (var p: players){
            result.add(p);
        }
        return result;
    }
}
