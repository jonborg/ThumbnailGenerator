package thumbnail.generate;

import fighter.Player;
import fighter.FighterArtType;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.var;
import thumbnail.image.settings.ImageSettings;
import tournament.Tournament;

@Getter
@Builder
public class ThumbnailSettings {
    private Tournament tournament;
    private ImageSettings imageSettings;
    private boolean locally;
    private String round;
    private String date;
    private List<Player> players;
    private FighterArtType artType;

    public static List<Player> createPlayerList(Player... players){
        var result = new ArrayList<Player>();
        for (var p: players){
            result.add(p);
        }
        return result;
    }
}





