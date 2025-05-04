package thumbnailgenerator.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
@Setter
public class GeneratedGraphic {
    Tournament tournament;
    Game game;
    boolean locally;
    List<Player> players;
    FighterArtTypeEnum artType;

    public static List<Player> createPlayerList(Player... players){
        var result = new ArrayList<Player>();
        for (var p: players){
            result.add(p);
        }
        return result;
    }
}
