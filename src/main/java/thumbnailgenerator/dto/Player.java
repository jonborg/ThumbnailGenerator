package thumbnailgenerator.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import thumbnailgenerator.enums.RivalsOfAether2Enum;
import thumbnailgenerator.enums.SmashUltimateEnum;
import thumbnailgenerator.enums.StreetFighter6Enum;
import thumbnailgenerator.enums.Tekken8Enum;


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

    public static List<Player> generatePreviewPlayers(Game game){
        List<Player> list = new ArrayList<>();
        var listChar1 = new ArrayList<Fighter>();
        var listChar2 = new ArrayList<Fighter>();

        var listName1 = new ArrayList<String>();
        var listName2 = new ArrayList<String>();

        switch(game) {
            //TODO Melee
            case SSBU:
                var smashEnumValues = SmashUltimateEnum.values();
                listName1.add(smashEnumValues[0].getName());
                listName1.add(smashEnumValues[0].getCode());
                listName2.add(smashEnumValues[1].getName());
                listName2.add(smashEnumValues[1].getCode());
                break;
            case ROA2:
                var roaEnumValues = RivalsOfAether2Enum.values();
                listName1.add(roaEnumValues[0].getName());
                listName1.add(roaEnumValues[0].getCode());
                listName2.add(roaEnumValues[1].getName());
                listName2.add(roaEnumValues[1].getCode());
                break;
            case SF6:
                var sf6EnumValues = StreetFighter6Enum.values();
                listName1.add(sf6EnumValues[0].getName());
                listName1.add(sf6EnumValues[0].getCode());
                listName2.add(sf6EnumValues[1].getName());
                listName2.add(sf6EnumValues[1].getCode());
                break;
            case TEKKEN8:
                var tekken8EnumValues = Tekken8Enum.values();
                listName1.add(tekken8EnumValues[0].getName());
                listName1.add(tekken8EnumValues[0].getCode());
                listName2.add(tekken8EnumValues[1].getName());
                listName2.add(tekken8EnumValues[1].getCode());
                break;
        }

        listChar1.add(new Fighter(listName1.get(0), listName1.get(1), 1, false));
        listChar2.add(new Fighter(listName2.get(0), listName2.get(1), 1, false));

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
