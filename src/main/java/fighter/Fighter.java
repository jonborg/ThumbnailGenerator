package fighter;

import java.awt.image.BufferedImage;

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


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }
}
