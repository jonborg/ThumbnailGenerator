package top8.generate;

import fighter.SmashUltimateFighterArtType;
import fighter.Player;
import java.util.List;

import fighter.name.Game;
import lombok.Builder;
import lombok.Getter;
import tournament.Tournament;

@Getter
@Builder
public class Top8Settings {

    private Tournament tournament;
    private Game game;
    private boolean locally;
    private List<Player> players;
    private SmashUltimateFighterArtType artType;

}

