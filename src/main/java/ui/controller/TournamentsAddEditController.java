package ui.controller;

import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import thumbnail.generate.Thumbnail;
import thumbnail.text.TextSettings;
import tournament.Tournament;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.Buffer;
import java.util.ResourceBundle;

public class TournamentsAddEditController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private TextField id;
    @FXML
    private TextField logo;
    @FXML
    private TextField foreground;
    @FXML
    private TextField background;

    @FXML
    private TextField font;
    @FXML
    private  TextField sizeTop;
    @FXML
    private  TextField angleTop;
    @FXML
    private  TextField sizeBottom;
    @FXML
    private  TextField angleBottom;
    @FXML
    private  CheckBox bold;
    @FXML
    private  CheckBox italic;
    @FXML
    private  CheckBox shadow;
    @FXML
    private  TextField contour;
    @FXML
    private  TextField downOffsetTopLeft;
    @FXML
    private  TextField downOffsetTopRight;
    @FXML
    private  TextField downOffsetBottomLeft;
    @FXML
    private  TextField downOffsetBottomRight;

    @FXML
    private Button cancelButton;
    @FXML
    private ImageView preview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tournament tournament = TournamentsSettingsController.getSelectedTournament();
        id.setText(tournament.getTournamentId());
        name.setText(tournament.getName());
        logo.setText(tournament.getImage());
        foreground.setText(tournament.getThumbnailForeground());
        background.setText(tournament.getThumbnailBackground());

        TextSettings textSettings = TextSettings.loadTextSettings(tournament.getTournamentId());
        font.setText(textSettings.getFont());
        sizeTop.setText(String.valueOf(textSettings.getSizeTop()));
        sizeBottom.setText(String.valueOf(textSettings.getSizeBottom()));
        angleTop.setText(String.valueOf(textSettings.getAngleTop()));
        angleBottom.setText(String.valueOf(textSettings.getAngleBottom()));
        bold.setSelected(textSettings.hasBold());
        italic.setSelected(textSettings.hasItalic());
        shadow.setSelected(textSettings.hasShadow());
        contour.setText(String.valueOf(textSettings.getContour()));
        downOffsetTopLeft.setText(String.valueOf(textSettings.getDownOffsetTop()[0]));
        downOffsetTopRight.setText(String.valueOf(textSettings.getDownOffsetTop()[1]));
        downOffsetBottomLeft.setText(String.valueOf(textSettings.getDownOffsetBottom()[0]));
        downOffsetBottomRight.setText(String.valueOf(textSettings.getDownOffsetBottom()[1]));
    }


    public void previewThumbnail(ActionEvent actionEvent) {

        Tournament tournament = new Tournament(id.getText(), name.getText(),
                logo.getText(), foreground.getText(), background.getText());

        TextSettings textSettings = new TextSettings(id.getText(), font.getText(),
                bold.isSelected(), italic.isSelected(), shadow.isSelected(), Float.parseFloat(contour.getText()),
                Integer.parseInt(sizeTop.getText()), Integer.parseInt(sizeBottom.getText()),
                Float.parseFloat(angleTop.getText()), Float.parseFloat(angleBottom.getText()),
                new int[]{Integer.parseInt(downOffsetTopLeft.getText()), Integer.parseInt(downOffsetTopRight.getText())},
                new int[]{Integer.parseInt(downOffsetBottomLeft.getText()), Integer.parseInt(downOffsetBottomRight.getText())});

        try{
            BufferedImage previewImage = Thumbnail.generatePreview(tournament, textSettings);
            Image image = SwingFXUtils.toFXImage(previewImage, null);
            preview.setImage(image);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
