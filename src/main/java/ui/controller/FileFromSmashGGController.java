package ui.controller;

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
import smashgg.match.SetGG;
import smashgg.query.QueryUtils;
import smashgg.tournament.EventGG;
import smashgg.tournament.PhaseGG;
import smashgg.tournament.PhaseGroupNodeGG;
import smashgg.tournament.TournamentGG;
import thumbnail.generate.ThumbnailFromFile;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

import java.net.URL;
import java.util.ResourceBundle;

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

        if (phaseGroupSelect.getSelectionModel().getSelectedItem() !=null
                && !phaseGroupSelect.getSelectionModel().getSelectedItem().isNull()){
            readSetsFromSmashGG(2);
            return;
        }

        if (phaseSelect.getSelectionModel().getSelectedItem() !=null
                && !phaseSelect.getSelectionModel().getSelectedItem().isNull()){
            readSetsFromSmashGG(1);
            return;
        }

        if (eventSelect.getSelectionModel().getSelectedItem() !=null
                && !eventSelect.getSelectionModel().getSelectedItem().isNull()){
            readSetsFromSmashGG(0);
            return;
        }
    }

    private void test(){}

    private void readSetsFromSmashGG(int mode){
        int totalPages=-1;
        int readPages=0;

        String query;
        String mainBody;
        String eventName = eventSelect.getSelectionModel().getSelectedItem().getName();

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
                if(setNodeGG.hasStream()){
                    foundSets.appendText(setNodeGG.toString());
                }
            });
        }while(readPages<totalPages);

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
                if (eventSelect.getItems().size() > 0){
                    eventSelect.getSelectionModel().select(0);
                }
            }
        });
    }
}
