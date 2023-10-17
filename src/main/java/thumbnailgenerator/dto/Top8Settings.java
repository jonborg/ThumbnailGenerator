package thumbnailgenerator.dto;

import thumbnailgenerator.service.SmashUltimateFighterArtType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Top8Settings {

    private Tournament tournament;
    private Game game;
    private boolean locally;
    private List<Player> players;
    private SmashUltimateFighterArtType artType;

}

