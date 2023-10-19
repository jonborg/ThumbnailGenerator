package thumbnailgenerator.dto;

import lombok.Setter;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Setter
@Builder
public class Top8 extends GeneratedGraphic {

    private Tournament tournament;
    private Game game;
    private boolean locally;
    private List<Player> players;
    private SmashUltimateFighterArtType artType;

    public FileTop8Settings getFileTop8Settings(){
        return getTournament().getTop8SettingsByGame(getGame());
    }
}

