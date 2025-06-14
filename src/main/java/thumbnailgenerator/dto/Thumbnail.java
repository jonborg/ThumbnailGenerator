package thumbnailgenerator.dto;

import java.awt.image.BufferedImage;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

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
    private FighterArtTypeEnum artType;
    private BufferedImage defaultForeground;
}





