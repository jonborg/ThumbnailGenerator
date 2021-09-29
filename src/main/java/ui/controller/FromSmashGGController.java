package ui.controller;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import exception.FontNotFoundException;
import exception.ThumbnailFromFileException;
import file.json.JSONReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.codehaus.plexus.util.ExceptionUtils;
import smashgg.match.SetGG;
import smashgg.match.StreamGG;
import smashgg.query.QueryUtils;
import smashgg.tournament.EventGG;
import smashgg.tournament.PhaseGG;
import smashgg.tournament.PhaseGroupNodeGG;
import smashgg.tournament.TournamentGG;
import thumbnail.generate.ThumbnailFromFile;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class FromSmashGGController implements Initializable {

    @FXML
    private TextField authToken;
    @FXML
    private TextField tournamentURL;
    @FXML
    private TextArea foundSets;
    @FXML
    private Button genText;
    @FXML
    private Button genStart;
    @FXML
    private ComboBox<EventGG> eventSelect;
    @FXML
    private ComboBox<PhaseGG> phaseSelect;
    @FXML
    private ComboBox<PhaseGroupNodeGG> phaseGroupSelect;
    @FXML
    private ComboBox<StreamGG> streamSelect;

    private Tournament backupTournament;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        removeSelectedTournament();
        resetComboBoxes(true);
        initTournamentFieldListener();
        initFoundSetsListener();
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
        try {
            QueryUtils.initClient(authToken.getText());
        }catch (IllegalArgumentException e){
            AlertFactory.displayError("Could not connect to Smash.gg due to a authorization token issue",
                    ExceptionUtils.getStackTrace(e));
            return;
        }
        if (phaseGroupSelect.getSelectionModel().getSelectedItem() != null
                && !phaseGroupSelect.getSelectionModel().getSelectedItem().isNull()){
            readSetsFromSmashGG(2);
            return;
        }

        if (phaseSelect.getSelectionModel().getSelectedItem() != null
                && !phaseSelect.getSelectionModel().getSelectedItem().isNull()){
            readSetsFromSmashGG(1);
            return;
        }

        if (eventSelect.getSelectionModel().getSelectedItem() != null
                && !eventSelect.getSelectionModel().getSelectedItem().isNull()){
            readSetsFromSmashGG(0);
            return;
        }
    }

    private void readSetsFromSmashGG(int mode){
        int totalPages=-1;
        int readPages=0;

        String query;
        String mainBody;
        String eventName = eventSelect.getSelectionModel().getSelectedItem().getName();
        try{
            do{
                switch (mode) {
                    case 0:
                        query= QueryUtils.getSetsByEvent(
                            eventSelect.getSelectionModel().getSelectedItem().getId(), ++readPages);
                        mainBody = "event";
                        break;

                    case 1:
                        query= QueryUtils.getSetsByPhase(
                            phaseSelect.getSelectionModel().getSelectedItem().getId(), ++readPages);
                        mainBody = "phase";
                        break;

                    case 2:
                        query= QueryUtils.getSetsByPhaseGroup(
                            phaseGroupSelect.getSelectionModel().getSelectedItem().getId(), ++readPages);
                        mainBody = "phaseGroup";
                        break;
                    default:
                        return;
                }
                JsonObject result = QueryUtils.runQuery(query);
                if (totalPages < 0){
                    System.out.println("Query ran: "+query);
                    totalPages = result.getAsJsonObject("data").getAsJsonObject(mainBody).getAsJsonObject("sets")
                            .getAsJsonObject("pageInfo").getAsJsonPrimitive("totalPages").getAsInt();
                    foundSets.setText(TournamentUtils.getSelectedTournament().getTournamentId() + ";" + eventName + System.lineSeparator());
                }
                SetGG set = (SetGG) JSONReader.getJSONObject(result.getAsJsonObject("data").getAsJsonObject(mainBody)
                        .getAsJsonObject("sets").toString(), new TypeToken<SetGG>() {}.getType());
                set.getSetNodes().forEach(setNodeGG -> {
                    if(setNodeGG.hasStream()) {
                        StreamGG selectedStream = streamSelect.getSelectionModel().getSelectedItem();
                        if(selectedStream == null || selectedStream.isNull() || selectedStream.getStreamName().equals(setNodeGG.getStreamName())) {
                            foundSets.appendText(setNodeGG.toString());
                        }
                    }
                });
            }while(readPages<totalPages);
            genStart.setDisable(false);
            AlertFactory.displayInfo("Successfully generated multi generation commands");
        }catch (ExecutionException | InterruptedException | NullPointerException e){
            AlertFactory.displayError("An issue occurred when executing query",
                    ExceptionUtils.getStackTrace(e));
            return;
        }finally {
            QueryUtils.closeClient();
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
            //AlertFactory already thrown inside ThumbnailFromFile.generateFromSmashGG
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
        if (!init) {
            eventSelect.getSelectionModel().clearSelection();
            phaseSelect.getSelectionModel().clearSelection();
            phaseGroupSelect.getSelectionModel().clearSelection();
            streamSelect.getSelectionModel().clearSelection();
            eventSelect.getItems().clear();
            phaseSelect.getItems().clear();
            phaseGroupSelect.getItems().clear();
            streamSelect.getItems().clear();
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
                getTournamentInfo();
            }
        });
    }

    private void getTournamentInfo(){
        if (tournamentURL.getText().isEmpty()){
            return;
        }
        if (!tournamentURL.getText().contains("smash.gg/tournament")){
            AlertFactory.displayError(tournamentURL.getText() + " is not a valid Smash.gg tournament URL. " +
                    "Ex.:https://smash.gg/tournament/swt-europe-ultimate-online-qualifier");
            return;
        }
        resetComboBoxes(false);
        try {
            QueryUtils.initClient(authToken.getText());
            JsonObject result = QueryUtils.runQuery(QueryUtils.tournamentDetailsQuery(tournamentURL.getText()));
            TournamentGG tournamentGG = (TournamentGG) JSONReader.getJSONObject(result.get("data")
                    .getAsJsonObject().get("tournament").toString(), new TypeToken<TournamentGG>() {}.getType());
            eventSelect.getItems().addAll(tournamentGG.getEvents());
            streamSelect.getItems().addAll(new StreamGG());
            streamSelect.getItems().addAll(tournamentGG.getStreams());
            if (eventSelect.getItems().size() > 0){
                eventSelect.getSelectionModel().select(0);
            }
            genText.setDisable(false);
        }catch (IllegalArgumentException e){
            AlertFactory.displayError("Could not connect to Smash.gg due to a authorization token issue",
                    ExceptionUtils.getStackTrace(e));
            genText.setDisable(true);

            return;
        }catch (ExecutionException | InterruptedException | NullPointerException e){
            AlertFactory.displayError("An issue occurred when executing query",
                    ExceptionUtils.getStackTrace(e));
            genText.setDisable(true);
            return;
        }finally {
            QueryUtils.closeClient();
        }
    }

    private void initFoundSetsListener() {
        foundSets.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (foundSets.getText() == null || foundSets.getText().isEmpty()) {
                genStart.setDisable(true);
            } else {
                genStart.setDisable(false);
            }
        });
    }


        public void showAuthTokenPage(ActionEvent actionEvent) {
        String url ="https://developer.smash.gg/docs/authentication/";
        if(Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            }catch(URISyntaxException | IOException e ){
                AlertFactory.displayError("Could not open the following URL: "+ url, e.getMessage());
            }
        }
    }
}
