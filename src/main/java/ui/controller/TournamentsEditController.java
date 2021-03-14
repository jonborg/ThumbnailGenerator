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
import java.util.stream.Stream;

public class TournamentsEditController extends TournamentsCreateController {


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

    @Override
    public void save(ActionEvent actionEvent) {
        Tournament currentTournament = generateTournamentWithCurrentSettings();
        if(!ThumbnailGeneratorController.getTournamentsSelectedEdit().updateDifferences(currentTournament)){
            ThumbnailGeneratorController.updateTournamentsList();
        }
        cancel(actionEvent);
    }

}
