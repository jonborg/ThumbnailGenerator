package thumbnailgenerator.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import thumbnailgenerator.enums.FatalFuryCotwEnum;
import thumbnailgenerator.enums.RivalsOfAether2Enum;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.StreetFighter6Enum;
import thumbnailgenerator.enums.Tekken8Enum;
import thumbnailgenerator.enums.interfaces.CharacterEnum;


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

    public static List<Player> generatePreviewPlayers(CharacterEnum[] characterEnums){
        List<Player> list = new ArrayList<>();
        var listChar1 = new ArrayList<Fighter>();
        var listChar2 = new ArrayList<Fighter>();

        var random = new Random();
        var char1 = characterEnums[random.nextInt(characterEnums.length)];
        var char12 = characterEnums[random.nextInt(characterEnums.length)];
        var char2 = characterEnums[random.nextInt(characterEnums.length)];
        var char22 = characterEnums[random.nextInt(characterEnums.length)];

        listChar1.add(new Fighter(char1.getName(), char1.getCode(), 1, false));
        listChar1.add(new Fighter(char12.getName(), char12.getCode(), 1, false));
        listChar2.add(new Fighter(char2.getName(), char2.getCode(), 1, false));
        listChar2.add(new Fighter(char22.getName(), char22.getCode(), 1, false));

        list.add(new Player("Player1", listChar1));
        list.add(new Player("Player2", listChar2));
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
