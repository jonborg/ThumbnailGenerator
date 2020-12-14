package tournament;

import com.google.gson.annotations.SerializedName;

public class Tournament {

    @SerializedName("id")
    private String tournamentId;
    @SerializedName("name")
    private String name;
    @SerializedName("logo")
    private String image;
    @SerializedName("foreground")
    private String foreground;
    @SerializedName("background")
    private String background;

    private static String defaultBackground= "assets/tournaments/backgrounds/default.png";


    public Tournament(String id, String name, String image, String foreground, String background){
        this.tournamentId = id;
        this.name = name;
        this.image = image;
        this.foreground = foreground;
        this.background = background;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getThumbnailForeground() { return foreground; }

    public String getThumbnailBackground() {
        if (this.background == null){
            return defaultBackground;
        }
        return background;
    }


}
