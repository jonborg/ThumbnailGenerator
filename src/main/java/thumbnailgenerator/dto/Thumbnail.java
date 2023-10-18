package thumbnailgenerator.dto;

import thumbnailgenerator.enums.SmashUltimateFighterArtType;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Thumbnail extends GraphicGenerated {
    private Tournament tournament;
    private Game game;
    private ImageSettings imageSettings;
    private boolean locally;
    private String round;
    private String date;
    private List<Player> players;
    private SmashUltimateFighterArtType artType;
}





