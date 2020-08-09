package ui.league;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class LeagueButton extends ToggleButton {

    private File image;
    private String id;
    private String foreground;
    private String league;
    private String template;

    public LeagueButton (){
        super();
    }

    public LeagueButton (File image, String id, String foreground, String league, String template){
        this.foreground = foreground;
        this.id = id;
        this.image = image;
        this.league = league;
        this.template = template;

        this.setGraphic(new ImageView(new Image(image.toURI().toString())));
    }

    public LeagueButton(File image) {
        this(image, null, null, null, null);

        if (image.getName().contains("throwdown.png")) {
            this.foreground = "foregroundLx.png";
            this.id = "throwdown";
            this.league = "Throwdown Lx";
            this.template = "invictaChart.png";
        }
        if (image.getName().contains("invicta.png")) {
            this.foreground = "foregroundPorto.png";
            this.id = "invicta";
            this.league = "Smash Invicta";
            this.template = "invictaChart.png";
        }
        if (image.getName().contains("sop.png")) {
            this.foreground = "foregroundSop.png";
            this.id = "sop";
            this.league = "Smash or Pass";
            this.template = "invictaChart.png";
        }
    }


    public File getImage() {
        return image;
    }

    public String getForeground() {
        return foreground;
    }

    public String getLeague() {
        return league;
    }

    public String getTemplate(){ return template;}

    public String getLeagueId() { return id; }
}
