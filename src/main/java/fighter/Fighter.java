package fighter;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Fighter  {

    private String  playerName;
    private String  name;
    private String  urlName;
    private int     alt=1;
    private boolean flip = false;

    public Fighter(){
        super();
    }

    public Fighter(String playerName, String name, String urlName,int alt, boolean flip){
        this.playerName = playerName;
        this.name = name;
        this.urlName = urlName;
        this.alt = alt;
        this.flip = flip;
    }

    public static List<Fighter> generatePreviewFighters(){
        List<Fighter> list = new ArrayList<>();
        list.add(new Fighter("Player 1","Mario", "mario", 1, false));
        list.add(new Fighter("Player 2","Sonic", "sonic", 1, false));
        return list;
    }
}
