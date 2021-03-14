package ui.controller;

import javafx.event.ActionEvent;
import org.codehaus.plexus.util.ExceptionUtils;
import thumbnail.text.TextSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

import java.net.URL;
import java.util.*;

public class TournamentsEditController extends TournamentsCreateController {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNumberTextFields();
        initFontDropdown();

        Tournament tournament = getSelectedEdit();
        id.setText(tournament.getTournamentId());
        name.setText(tournament.getName());
        logo.setText(tournament.getImage());
        foreground.setText(tournament.getThumbnailForeground());
        background.setText(tournament.getThumbnailBackground());

        TextSettings textSettings = tournament.getTextSettings();
        try {
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
        }catch(Exception e){
            AlertFactory.displayError("Could not load text settings for tournament " + tournament.getName()
                            + ". Please check text settings file for setting for tournament with id of "
                            + tournament.getTournamentId(), ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public void save(ActionEvent actionEvent) {
        Tournament currentTournament = generateTournamentWithCurrentSettings();
        if(!getSelectedEdit().updateDifferences(currentTournament)){
            updateTournamentsListAndSettings();
        }
        cancel(actionEvent);
    }

    private static Tournament getSelectedEdit(){
        return TournamentUtils.getSelectedEdit();
    }



}
