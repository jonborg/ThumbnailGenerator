package top8.generate;

import fighter.FighterArtType;
import fighter.Player;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import thumbnail.image.settings.ImageSettings;
import tournament.Tournament;

@Getter
@Builder
public class Top8Settings {
    private Tournament tournament;
    private ImageSettings imageSettings;
    private boolean locally;
    private String date;
    private String edition;
    private List<Player> players;
    private FighterArtType artType;

}

