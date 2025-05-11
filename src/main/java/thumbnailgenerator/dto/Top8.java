package thumbnailgenerator.dto;

import lombok.Setter;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Getter
@Setter
@Builder
public class Top8 extends GeneratedGraphic {

    private Tournament tournament;
    private Game game;
    private boolean locally;
    private List<Player> players;
    private FighterArtTypeEnum artType;
}

