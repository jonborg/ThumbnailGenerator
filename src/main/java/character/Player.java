package character;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Player {
    private String  playerName;
    private List<Character> characterList;

    public static List<Player> generatePreviewCharacters(){
        List<Player> list = new ArrayList<>();
        var listChar1 = new ArrayList<Character>();
        var listChar2 = new ArrayList<Character>();

        listChar1.add(new Character("Mario", "mario", 1, false));
        listChar2.add(new Character("Sonic", "sonic", 1, false));

        list.add(new Player("Player1", listChar1));
        list.add(new Player("Player1", listChar2));
        return list;
    }

    public Character getCharacter(int index){
        return this.characterList.get(index);
    }

    public void setCharacterFlip(int index, boolean flip){
        this.characterList.get(index).setFlip(flip);
    }
}
