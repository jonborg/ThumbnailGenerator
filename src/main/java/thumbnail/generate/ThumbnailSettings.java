package thumbnail.generate;

import fighter.Fighter;
import fighter.FighterArtType;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.var;
import thumbnail.image.ImageSettings;
import tournament.Tournament;

@Getter
@Builder
public class ThumbnailSettings {
    private Tournament tournament;
    private ImageSettings imageSettings;
    private boolean locally;
    private String round;
    private String date;
    private List<Fighter> fighters;
    private FighterArtType artType;

    public static List<Fighter> createFighterList(Fighter... fighters){
        var result =new ArrayList<Fighter>();
        for (var f: fighters){
            result.add(f);
        }
        return result;
    }

}





