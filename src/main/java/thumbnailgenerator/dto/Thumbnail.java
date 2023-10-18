package thumbnailgenerator.dto;

import lombok.Setter;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Setter
@Builder
public class Thumbnail extends GeneratedGraphic {
    private Tournament tournament;
    private Game game;
    private ImageSettings imageSettings;
    private boolean locally;
    private String round;
    private String date;
    private List<Player> players;
    private SmashUltimateFighterArtType artType;
}





