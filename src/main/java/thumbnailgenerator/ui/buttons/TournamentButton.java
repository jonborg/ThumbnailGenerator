package thumbnailgenerator.ui.buttons;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.Tournament;

@Getter
public class TournamentButton extends ToggleButton {

    private Tournament tournament;
    private static int imageSize = 100;
    private static int buttonSize = 110;

    public TournamentButton(Tournament tournament) {
        super();
        this.tournament = tournament;

        if (this.getImage() == null) {
            this.setText(this.getName());
        } else {
            ImageView imageView = new ImageView(new Image("file:"+this.getImage()));
            if(imageView.getImage().getWidth() > imageView.getImage().getHeight()){
                imageView.setFitWidth(imageSize);
            }else{
                imageView.setFitHeight(imageSize);
            }
            imageView.setPreserveRatio(true);
            this.setGraphic(imageView);
        }
        this.setMinSize(buttonSize,buttonSize);
        this.setPrefSize(buttonSize,buttonSize);
        this.setMaxSize(buttonSize,buttonSize);
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
}
