package ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import thumbnail.generate.Thumbnail;
import thumbnail.text.TextSettings;
import tournament.Tournament;
import ui.combobox.InputFilter;
import ui.textfield.ChosenFileField;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class TournamentsCreateController implements Initializable {

    @FXML
    protected TextField name;
    @FXML
    protected TextField id;
    @FXML
    protected ChosenFileField logo;
    @FXML
    protected ChosenFileField foreground;
    @FXML
    protected ChosenFileField background;

    @FXML
    protected ComboBox<String> font;
    @FXML
    protected  TextField sizeTop;
    @FXML
    protected  TextField angleTop;
    @FXML
    protected  TextField sizeBottom;
    @FXML
    protected  TextField angleBottom;
    @FXML
    protected  CheckBox bold;
    @FXML
    protected  CheckBox italic;
    @FXML
    protected  CheckBox shadow;
    @FXML
    protected  TextField contour;
    @FXML
    protected  TextField downOffsetTopLeft;
    @FXML
    protected  TextField downOffsetTopRight;
    @FXML
    protected  TextField downOffsetBottomLeft;
    @FXML
    protected  TextField downOffsetBottomRight;

    @FXML
    protected Button cancelButton;
    @FXML
    protected ImageView preview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFontDropdown();
    }


    public void previewThumbnail(ActionEvent actionEvent) {
        Tournament tournament = generateTournamentWithCurrentSettings();

        try{
            BufferedImage previewImage = Thumbnail.generatePreview(tournament);
            Image image = SwingFXUtils.toFXImage(previewImage, null);
            preview.setImage(image);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    protected Tournament generateTournamentWithCurrentSettings(){
        Tournament tournament =  new Tournament(id.getText(), name.getText(),
                logo.getText(), foreground.getText(), background.getText());
        TextSettings textSettings = new TextSettings(id.getText(), font.getSelectionModel().getSelectedItem(),
                bold.isSelected(), italic.isSelected(), shadow.isSelected(), Float.parseFloat(contour.getText()),
                Integer.parseInt(sizeTop.getText()), Integer.parseInt(sizeBottom.getText()),
                Float.parseFloat(angleTop.getText()), Float.parseFloat(angleBottom.getText()),
                new int[]{Integer.parseInt(downOffsetTopLeft.getText()), Integer.parseInt(downOffsetTopRight.getText())},
                new int[]{Integer.parseInt(downOffsetBottomLeft.getText()), Integer.parseInt(downOffsetBottomRight.getText())});
        tournament.setTextSettings(textSettings);

        return tournament;
    }

    protected void initFontDropdown(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        String[] allCompatibleFonts =  Stream.concat(Arrays.stream(fontFamilies),
                Arrays.stream(new String[]{"BebasNeue-Regular","Pikmin-Normal"})).sorted()
                .toArray(String[]::new);

        ObservableList<String> items = FXCollections.observableArrayList(allCompatibleFonts);
        FilteredList<String> filteredItems = new FilteredList<>(items);

        font.getEditor().textProperty().addListener(new InputFilter(font, filteredItems, false));
        font.setItems(filteredItems);
    }

    public void save(ActionEvent actionEvent) {
        Tournament currentTournament = generateTournamentWithCurrentSettings();
        ThumbnailGeneratorController.updateTournamentsListAndSettings(currentTournament);
        cancel(actionEvent);
    }

    public void cancel(ActionEvent actionEvent)  {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
