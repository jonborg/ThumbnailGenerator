package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SetNodeGG{
    @Expose
    @SerializedName("fullRoundText")
    private String roundName;
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

    public List<GameGG> getGames() {
        return games;
    }

    public StreamGG getStream() {
        return stream;
    }

    public String getStreamName() {
        return stream.getStreamName();
    }

    public Boolean hasStream(){
        return stream != null && getStreamName() != null;
    }

    public EntrantGG getEntrant(int entrantIndex){
        return games.get(0).getSelections().get(entrantIndex).getEntrant();
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
        return CharacterId.map.get(mostUsedCharacter);
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

        return player1NoTeam + ";" + player2NoTeam + ";"
                + getEntrantCharacter(player1) + ";" + getEntrantCharacter(player2) + ";"
                + "1;1;"
                + getRoundName()
                + System.lineSeparator();
    }
}

