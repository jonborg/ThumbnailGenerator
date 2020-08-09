package placement;

import fighter.Fighter;

import java.util.ArrayList;

public class Placement {
    private int placementNumber;
    private String  playerName;
    private ArrayList<Fighter>  fighters;

    public Placement(){
        super();
    }

    public Placement(int placementNumber, String playerName, ArrayList<Fighter> fighters){
        this.placementNumber = placementNumber;
        this.playerName = playerName;
        this.fighters = fighters;
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ArrayList<Fighter> getFighters() { return fighters; }

    public void setFighters(ArrayList<Fighter> fighters) {
        this.fighters = fighters;
    }

    public int getPlacementNumber() {
        return placementNumber;
    }

    public void setPlacementNumber(int placementNumber) {
        this.placementNumber = placementNumber;
    }
}
