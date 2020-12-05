package ui.tournament;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;

public class Tournament extends ToggleButton {

    private String tournamentId;
    private String name;
    private String image;
    private String foreground;
    private String background;

    private static String defaultBackground= "assets/tournaments/backgrounds/default.png";

    public Tournament(){
        super();
    }

    public Tournament(String id, String name, String image, String foreground, String background){
        this.tournamentId = id;
        this.name = name;
        this.image = image;
        this.foreground = foreground;
        this.background = background;

        if (this.image == null) {
            this.setText(this.name);
        } else {
            this.setGraphic(new ImageView(new Image("file:"+this.image)));
        }
    }

    public Tournament(JSONObject tournament){
        this((String) tournament.get("id"), (String) tournament.get("name"),
            tournament.containsKey("logo") ? (String) tournament.get("logo") : null,
            tournament.containsKey("foreground") ? (String) tournament.get("foreground") : null,
            tournament.containsKey("background") ? (String) tournament.get("background") : defaultBackground
            );
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

    public String getThumbnailBackground() { return background; }


}
