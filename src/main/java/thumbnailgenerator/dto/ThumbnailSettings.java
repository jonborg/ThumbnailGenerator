package thumbnailgenerator.dto;

import thumbnailgenerator.service.SmashUltimateFighterArtType;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.var;

@Getter
@Builder
public class ThumbnailSettings {
    private Tournament tournament;
    private Game game;
    private ImageSettings imageSettings;
    private boolean locally;
    private String round;
    private String date;
    private List<Player> players;
    private SmashUltimateFighterArtType artType;

    public static List<Player> createPlayerList(Player... players){
        var result = new ArrayList<Player>();
        for (var p: players){
            result.add(p);
        }
        return result;
    }
}





