package top8.generate;

import fighter.FighterArtType;
import fighter.Player;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import tournament.Tournament;

@Getter
@Builder
public class Top8Settings {
    private Tournament tournament;
    private boolean locally;
    private List<Player> players;
    private FighterArtType artType;

}

