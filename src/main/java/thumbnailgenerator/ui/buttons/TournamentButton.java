package thumbnailgenerator.ui.buttons;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import thumbnailgenerator.dto.Tournament;

@Getter
public class TournamentButton extends ToggleButton {

    private Tournament tournament;

    public TournamentButton(Tournament tournament) {
        super();
        this.tournament = tournament;

        if (this.getImage() == null) {
            this.setText(this.getName());
        } else {
            ImageView imageView = new ImageView(new Image("file:"+this.getImage()));
            if(imageView.getImage().getWidth() > imageView.getImage().getHeight()){
                imageView.setFitWidth(100);
            }else{
                imageView.setFitHeight(100);
            }
            imageView.setPreserveRatio(true);
            this.setGraphic(imageView);
        }
        this.setMinSize(110,110);
        this.setPrefSize(110,110);
        this.setMaxSize(110,110);
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
        return this.getTournament().getThumbnailSettings().getForeground();
    }

    public String getThumbnailBackground() {
        return this.getTournament().getThumbnailSettings().getBackground();
    }
}
