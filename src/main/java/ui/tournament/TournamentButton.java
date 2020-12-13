package ui.tournament;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class TournamentButton extends ToggleButton {

    private Tournament tournament;


    public TournamentButton(Tournament tournament) {
        super();
        this.tournament = tournament;

        if (this.getImage() == null) {
            this.setText(this.getName());
        } else {
            this.setGraphic(new ImageView(new Image("file:"+this.getImage())));
        }
        this.setPrefSize(110,110);
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public String getTournamentId() {
        return this.getTournament().getTournamentId();
    }

    public String getName() {
        return this.getTournament().getName();
    }

    public String getImage(){
        return this.getTournament().getImage();
    }

    public String getThumbnailForeground() {
        return this.getTournament().getThumbnailForeground();
    }

    public String getThumbnailBackground() {
        return this.getTournament().getThumbnailBackground();
    }
}
