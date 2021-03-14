package ui.controller;

import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
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
import json.JSONWriter;
import thumbnail.generate.Thumbnail;
import thumbnail.text.TextSettings;
import tournament.Tournament;
import ui.combobox.InputFilter;
import ui.textfield.ChosenFileField;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class TournamentsAddEditController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private TextField id;
    @FXML
    private ChosenFileField logo;
    @FXML
    private ChosenFileField foreground;
    @FXML
    private ChosenFileField background;

    @FXML
    private ComboBox<String> font;
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
        initFontDropdown();

        Tournament tournament = ThumbnailGeneratorController.getTournamentsSelectedEdit();
        id.setText(tournament.getTournamentId());
        name.setText(tournament.getName());
        logo.setText(tournament.getImage());
        foreground.setText(tournament.getThumbnailForeground());
        background.setText(tournament.getThumbnailBackground());

        TextSettings textSettings = tournament.getTextSettings();
        font.getSelectionModel().select(textSettings.getFont());
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
        Tournament tournament = generateTournamentWithCurrentSettings();

        try{
            BufferedImage previewImage = Thumbnail.generatePreview(tournament);
            Image image = SwingFXUtils.toFXImage(previewImage, null);
            preview.setImage(image);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private Tournament generateTournamentWithCurrentSettings(){
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

    private void initFontDropdown(){
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
        if(!ThumbnailGeneratorController.getTournamentsSelectedEdit().updateDifferences(currentTournament)){
            ThumbnailGeneratorController.updateTournamentsList();
        }
        cancel(actionEvent);
    }

    public void cancel(ActionEvent actionEvent)  {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
