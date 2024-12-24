package fighter;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class Player {
    private String  playerName;
    private List<Fighter> fighterList;

    public Player(String playerName, String name, String urlName,int alt, boolean flip){
        this.playerName = playerName;
        this.fighterList = new ArrayList<>();
        this.fighterList.add(new Fighter(name, urlName, alt, flip));
    }

    public static List<Player> generatePreviewPlayers(){
        List<Player> list = new ArrayList<>();
        var listChar1 = new ArrayList<Fighter>();
        var listChar2 = new ArrayList<Fighter>();

        listChar1.add(new Fighter("Mario", "mario", 1, false));
        listChar2.add(new Fighter("Sonic", "sonic", 1, false));

        list.add(new Player("Player1", listChar1));
        list.add(new Player("Player1", listChar2));
        return list;
    }

    public Fighter getFighter(int index){
        return this.fighterList.get(index);
    }

    public List<Fighter> getSecondaryFighters(){
        return this.fighterList.subList(1,this.fighterList.size());
    }

    public void setFighterFlip(int index, boolean flip){
        this.fighterList.get(index).setFlip(flip);
    }
}
