package thumbnailgenerator.dto;

import lombok.Setter;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtType;

@Getter
@Setter
@Builder
public class Top8 extends GeneratedGraphic {

    private Tournament tournament;
    private Game game;
    private boolean locally;
    private List<Player> players;
    private FighterArtType artType;

    public FileTop8Settings getFileTop8Settings(){
        return getTournament().getTop8SettingsByGame(getGame());
    }
}

