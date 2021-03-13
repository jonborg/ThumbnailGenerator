package ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
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
    private ImageView preview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       // preview.setImage(new Image(getClass().getResourceAsStream("/thumbnails/Aegis-pyra7--UrQte-sephiroth1--POOLS ROUND 1-12_03_2022.png")));

    }






}
