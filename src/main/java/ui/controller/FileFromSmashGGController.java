package ui.controller;

import com.github.gpluscb.ggjava.api.GGClient;
import com.github.gpluscb.ggjava.api.RateLimiter;
import com.github.gpluscb.ggjava.entity.object.response.GGResponse;
import com.github.gpluscb.ggjava.entity.object.response.ListResponse;
import com.github.gpluscb.ggjava.entity.object.response.objects.QueryResponse;
import com.github.gpluscb.ggjava.entity.object.response.objects.SetResponse;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import exception.FontNotFoundException;
import exception.ThumbnailFromFileException;
import file.json.JSONReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.codehaus.plexus.util.ExceptionUtils;
import smashgg.match.SetGG;
import smashgg.query.QueryUtils;
import smashgg.tournament.EventGG;
import smashgg.tournament.PhaseGG;
import smashgg.tournament.PhaseGroupNodeGG;
import smashgg.tournament.TournamentGG;
import thumbnail.generate.ThumbnailFromFile;
import smashgg.match.SetNodeGG;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FileFromSmashGGController implements Initializable {

    @FXML
    private TextField tournamentURL;
    @FXML
    private TextField fileName;
    @FXML
    private TextArea foundSets;
    @FXML
    private ComboBox<EventGG> eventSelect;
    @FXML
    private ComboBox<PhaseGG> phaseSelect;
    @FXML
    private ComboBox<PhaseGroupNodeGG> phaseGroupSelect;

    private Tournament backupTournament;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        removeSelectedTournament();
        QueryUtils.initClient();
        eventSelect.setDisable(true);
        resetComboBoxes(true);
        initTournamentFieldListener();
    }

    private void removeSelectedTournament(){
        backupTournament = TournamentUtils.getSelectedTournament();
        TournamentUtils.setSelectedTournament(null);
    }


    public void generateCommands() {
        if (TournamentUtils.getSelectedTournament() == null){
            AlertFactory.displayWarning("A tournament must be chosen before generating thumbnails.");
            return;
        }

        EventGG selection = eventSelect.getSelectionModel().getSelectedItem();
        if (eventSelect.getSelectionModel().getSelectedItem() !=null
                && !eventSelect.getSelectionModel().getSelectedItem().isNull()){
            int totalPages=-1;
            int readPages=0;
            do {
                JsonObject result = QueryUtils.runQuery(QueryUtils.getSetsByEvents(selection.getId(),++readPages));
                if (totalPages < 0){
                    totalPages = result.getAsJsonObject("data").getAsJsonObject("event").getAsJsonObject("sets")
                            .getAsJsonObject("pageInfo").getAsJsonPrimitive("totalPages").getAsInt();
                    foundSets.setText(TournamentUtils.getSelectedTournament().getTournamentId() + ";" + selection.getName() + System.lineSeparator());
                }

                SetGG set = (SetGG) JSONReader.getJSONObject(result.getAsJsonObject("data").getAsJsonObject("event")
                        .getAsJsonObject("sets").toString(), new TypeToken<SetGG>() {}.getType());
                set.getSetNodes().forEach(setNodeGG -> {
                    if(setNodeGG.hasStream()){
                        foundSets.appendText(setNodeGG.toString());
                    }
                });

            }while(readPages<totalPages);
        }
    }

    public void generateThumbnails(){
        if (foundSets.getText()==null || foundSets.getText().isEmpty()){
            AlertFactory.displayWarning("No commands were generated to create thumbnails");
            return;
        }
        try {
            ThumbnailFromFile.generateFromSmashGG(foundSets.getText());
            AlertFactory.displayInfo("Thumbnails were successfully generated and saved!");
        }catch(ThumbnailFromFileException e){
            //AlertFactory already thrown inside tbf.generateFromFile
        }catch(FontNotFoundException e){
            AlertFactory.displayError(e.getMessage());
        }
    }



    public Tournament getBackupTournament(){
        return this.backupTournament;
    }

    private void resetComboBoxes(boolean init){
        phaseSelect.setDisable(true);
        phaseGroupSelect.setDisable(true);
        if (init) {
            eventSelect.setDisable(true);
        }else{
            eventSelect.setDisable(false);
            eventSelect.getSelectionModel().clearSelection();
            phaseSelect.getSelectionModel().clearSelection();
            phaseGroupSelect.getSelectionModel().clearSelection();
            eventSelect.getItems().clear();
            phaseSelect.getItems().clear();
            phaseGroupSelect.getItems().clear();
            eventSelect.getItems().add(new EventGG());
        }
    }

    public void eventListener(ActionEvent actionEvent) {
        EventGG selection = eventSelect.getSelectionModel().getSelectedItem();
        phaseSelect.getSelectionModel().clearSelection();
        phaseGroupSelect.getSelectionModel().clearSelection();
        phaseSelect.getItems().clear();
        phaseGroupSelect.getItems().clear();
        if (selection == null || selection.isNull()){
            phaseSelect.setDisable(true);
            phaseGroupSelect.setDisable(true);
        }else{
            phaseSelect.setDisable(false);
            phaseSelect.getItems().add(new PhaseGG());
            phaseSelect.getItems().addAll(selection.getPhases());
        }
    }

    public void phaseListener(ActionEvent actionEvent) {
        PhaseGG selection = phaseSelect.getSelectionModel().getSelectedItem();
        phaseGroupSelect.getSelectionModel().clearSelection();
        phaseGroupSelect.getItems().clear();
        if (selection == null || selection.isNull()){
            phaseGroupSelect.setDisable(true);
        }else{
            phaseGroupSelect.setDisable(false);
            phaseGroupSelect.getItems().add(new PhaseGroupNodeGG());
            phaseGroupSelect.getItems().addAll(selection.getPhaseGroups());
        }
    }

    private void initTournamentFieldListener() {
        tournamentURL.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if (tournamentURL.getText().isEmpty()){
                    return;
                }
                if (!tournamentURL.getText().contains("smash.gg/tournament")){
                    AlertFactory.displayError(tournamentURL.getText() + "is not a valid Smash.gg tournament URL. " +
                            "Ex.:https://smash.gg/tournament/swt-europe-ultimate-online-qualifier");
                    return;
                }
                resetComboBoxes(false);
                JsonObject result = QueryUtils.runQuery(QueryUtils.tournamentDetailsQuery(tournamentURL.getText()));
                TournamentGG tournamentGG = (TournamentGG) JSONReader.getJSONObject(result.get("data")
                        .getAsJsonObject().get("tournament").toString(), new TypeToken<TournamentGG>() {}.getType());
                eventSelect.getItems().addAll(tournamentGG.getEvents());

            }
        });
    }
}
