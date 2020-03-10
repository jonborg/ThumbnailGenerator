package ui;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class LeagueButton extends ToggleButton {

    private File image;
    private String foreground;
    private String league;

    public LeagueButton (){
        super();
    }

    public LeagueButton (File image, String foreground, String league){
        this.foreground = foreground;
        this.image = image;
        this.league = league;

        this.setGraphic(new ImageView(new Image(image.toURI().toString())));
    }

    public LeagueButton(File image) {
        this(image, null, null);

        if (image.getName().contains("throwdown.png")) {
            this.foreground = "foregroundLx.png";
            this.league = "Throwdown Lx";
        }
        if (image.getName().contains("invicta.png")) {
            this.foreground = "foregroundPorto.png";
            this.league = "Smash Invicta";
        }
        if (image.getName().contains("sop.png")) {
            this.foreground = "foregroundSop.png";
            this.league = "Smash or Pass";
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

}
