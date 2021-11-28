package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Getter
public class SetNodeGG{
    @Expose
    @SerializedName("fullRoundText")
    private String roundName;
    @Expose
    @SerializedName("slots")
    private List<SlotGG> slots;
    @Expose
    @SerializedName("games")
    private List<GameGG> games;
    @Expose
    @SerializedName("stream")
    private StreamGG stream;

    public String getRoundName() {
        if (roundName.contains("-Final")) {
            return roundName.replace("-Final", "s");
        }
        if (roundName.contains("Final")){
            return roundName + "s";
        }
        return roundName;
    }

    public String getStreamName() {
        return stream.getStreamName();
    }

    public Boolean hasStream(){
        return stream != null && getStreamName() != null;
    }

    public EntrantGG getEntrant(int entrantIndex){
        return slots.get(entrantIndex).getEntrant();
    }

    public String getEntrantCharacter(String entrantName){
        HashMap<Integer,Integer> charSel = new HashMap<>();
        for (GameGG gameGG :games) {
            for (SelectionGG selectionGG : gameGG.getSelections()) {
                if (selectionGG.getEntrant().getName().equals(entrantName)) {
                    int character = selectionGG.getSelectionValue();
                    if (charSel.containsKey(character)) {
                        charSel.put(character, charSel.get(character) + 1);
                    }else{
                        charSel.put(character, 1);
                    }
                    break;
                }
            }
        }
        int mostUsedCharacter = Collections.max(charSel.entrySet(), HashMap.Entry.comparingByValue()).getKey();
        return CharacterId.getValue(mostUsedCharacter);
    }

    private String getEntrateNameWithNoTeam(String name){
        return name.contains(" | ") ? name.split(" \\| ")[1].trim() : name;
    }

    @Override
    public String toString(){
        String player1 = getEntrant(0).getName();
        String player2 = getEntrant(1).getName();

        String player1NoTeam = getEntrateNameWithNoTeam(player1);
        String player2NoTeam = getEntrateNameWithNoTeam(player2);

        String characters;

        if (games == null){
            characters = "CHAR1;CHAR2";
        }else{
            characters = getEntrantCharacter(player1) + ";" + getEntrantCharacter(player2);
        }

        return player1NoTeam + ";" + player2NoTeam + ";"
                + characters + ";"
                + "1;1;"
                + getRoundName();
    }
}

