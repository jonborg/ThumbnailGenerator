import java.awt.image.BufferedImage;

public class Fighter  {



    private String  playerName;
    private String  urlName;
    private int     alt=1;
    private boolean flip = false;

    public Fighter(){
        super();
    }

    public Fighter(String playerName, String urlName,int alt, boolean flip){
        this.playerName = playerName;
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
