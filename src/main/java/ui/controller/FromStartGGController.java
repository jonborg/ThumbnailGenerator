package ui.controller;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import exception.FontNotFoundException;
import exception.ThumbnailFromFileException;
import file.json.JSONReader;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import startgg.match.SetGG;
import startgg.match.StreamGG;
import startgg.query.QueryUtils;
import startgg.tournament.EventGG;
import startgg.tournament.PhaseGG;
import startgg.tournament.PhaseGroupNodeGG;
import startgg.tournament.TournamentGG;
import thumbnail.generate.ThumbnailFromFile;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

public class FromStartGGController implements Initializable {
    private final Logger LOGGER = LogManager.getLogger(FromStartGGController.class);

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
    @FXML
    private CheckBox saveLocally;

    private Tournament backupTournament;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("User selected thumbnail generation with Start.gg tournament information.");
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
        LOGGER.info("Generating commands for thumbnail generation.");
        if (TournamentUtils.getSelectedTournament() == null){
            LOGGER.error("User did not select a tournament.");
            AlertFactory.displayWarning("A tournament must be chosen before generating thumbnails.");
            return;
        }
        try {
            LOGGER.debug("Loading provided authorization token.");
            QueryUtils.initClient(authToken.getText());
        }catch (IllegalArgumentException e){
            LOGGER.error("Could not connect to Start.gg due to a authorization token issue.");
            LOGGER.catching(e);
            AlertFactory.displayError("Could not connect to Start.gg due to a authorization token issue",
                    ExceptionUtils.getStackTrace(e));
            return;
        }
        if (phaseGroupSelect.getSelectionModel().getSelectedItem() != null
                && !phaseGroupSelect.getSelectionModel().getSelectedItem().isNull()){
            LOGGER.debug("Reading sets of phase group {}",phaseGroupSelect.getSelectionModel().getSelectedItem().getIdentifier());
            readSetsFromSmashGG(2);
            return;
        }

        if (phaseSelect.getSelectionModel().getSelectedItem() != null
                && !phaseSelect.getSelectionModel().getSelectedItem().isNull()){
            LOGGER.debug("Reading sets of phase {}",phaseSelect.getSelectionModel().getSelectedItem().getName());
            readSetsFromSmashGG(1);
            return;
        }

        if (eventSelect.getSelectionModel().getSelectedItem() != null
                && !eventSelect.getSelectionModel().getSelectedItem().isNull()){
            LOGGER.debug("Reading sets of event {}",eventSelect.getSelectionModel().getSelectedItem().getName());
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
                LOGGER.debug("Running query -> {}", query);
                JsonObject result = QueryUtils.runQuery(query);
                LOGGER.debug("Result -> {}", result.toString());

                if (totalPages < 0){
                    totalPages = result.getAsJsonObject("data").getAsJsonObject(mainBody).getAsJsonObject("sets")
                            .getAsJsonObject("pageInfo").getAsJsonPrimitive("totalPages").getAsInt();
                    foundSets.setText(TournamentUtils.getSelectedTournament().getTournamentId() + ";" + eventName + ";RENDER"+ System.lineSeparator());
                }
                SetGG set = (SetGG) JSONReader.getJSONObject(result.getAsJsonObject("data").getAsJsonObject(mainBody)
                        .getAsJsonObject("sets").toString(), new TypeToken<SetGG>() {}.getType());
                set.getSetNodes().forEach(setNodeGG -> {
                    if(setNodeGG.hasStream()) {
                        StreamGG selectedStream = streamSelect.getSelectionModel().getSelectedItem();
                        if(selectedStream == null || selectedStream.isNull()) {
                            LOGGER.debug("Found set -> {}", setNodeGG.toString());
                            foundSets.appendText(setNodeGG.toString()+System.lineSeparator());
                        } else {
                            LOGGER.info("Filtering sets by stream {}.", selectedStream.getStreamName());
                            if(selectedStream.getStreamName().equals(setNodeGG.getStreamName())){
                                LOGGER.debug("Found set -> {}", setNodeGG.toString());
                                foundSets.appendText(setNodeGG.toString()+System.lineSeparator());
                            }
                        }
                    }
                });
            }while(readPages<totalPages);
            setDisableGeneration(false);
            LOGGER.info("Finished generating multiple thumbnails generation commands.");
            AlertFactory.displayInfo("Finished generating multiple thumbnails generation commands.");
        }catch (ExecutionException | InterruptedException | NullPointerException e){
            LOGGER.error("An issue occurred when executing query");
            LOGGER.catching(e);
            AlertFactory.displayError("An issue occurred when executing query",
                    ExceptionUtils.getStackTrace(e));
            return;
        }finally {
            QueryUtils.closeClient();
        }
    }

    public void generateThumbnails(){
        LOGGER.info("Starting multiple thumbnails generation.");
        if (foundSets.getText()==null || foundSets.getText().isEmpty()){
            LOGGER.error("No commands were generated to create thumbnails.");
            AlertFactory.displayWarning("No commands were generated to create thumbnails");
            return;
        }
        try {
            ThumbnailFromFile.generateFromSmashGG(foundSets.getText(), saveLocally.isSelected());
            LOGGER.info("Thumbnails were successfully generated and saved.");
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
            LOGGER.info("Event selection was removed. Resetting phase and phase group lists.");
            phaseSelect.setDisable(true);
            phaseGroupSelect.setDisable(true);
        }else{
            LOGGER.info("Event {} was selected. Setting phase list.", selection.getName());
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
            LOGGER.info("Phase selection was removed. Resetting phase group lists.");
            phaseGroupSelect.setDisable(true);
        }else{
            LOGGER.info("Phase {} was selected. Setting phase groups list.", selection);
            phaseGroupSelect.setDisable(false);
            phaseGroupSelect.getItems().add(new PhaseGroupNodeGG());
            phaseGroupSelect.getItems().addAll(selection.getPhaseGroups());
        }
    }

    public void phaseGroupListener(ActionEvent actionEvent) {
        PhaseGroupNodeGG selection = phaseGroupSelect.getSelectionModel().getSelectedItem();
        if (selection == null || selection.isNull()){
            LOGGER.info("Phase Group selection was removed.");
        }else {
            LOGGER.info("Phase Group {} was selected.", selection);
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
        if (!tournamentURL.getText().contains("start.gg/tournament")){
            LOGGER.error("User provided a not valid Start.gg tournament URL -> {}", tournamentURL.getText());
            AlertFactory.displayError(tournamentURL.getText() + " is not a valid Start.gg tournament URL. " +
                    "Ex.:https://start.gg/tournament/swt-europe-ultimate-online-qualifier");
            return;
        }
        resetComboBoxes(false);
        try {
            LOGGER.debug("Loading provided authorization token.");
            QueryUtils.initClient(authToken.getText());
            LOGGER.info("Loading tournament details from Start.gg.");
            LOGGER.debug("Running query -> {}", QueryUtils.tournamentDetailsQuery(tournamentURL.getText()));
            JsonObject result = QueryUtils.runQuery(QueryUtils.tournamentDetailsQuery(tournamentURL.getText()));
            LOGGER.debug("Result -> {}", result.toString());

            TournamentGG tournamentGG = (TournamentGG) JSONReader.getJSONObject(result.get("data")
                    .getAsJsonObject().get("tournament").toString(), new TypeToken<TournamentGG>() {}.getType());
            LOGGER.debug("Setting events list.");
            eventSelect.getItems().addAll(tournamentGG.getEvents());

            streamSelect.getItems().addAll(new StreamGG());
            LOGGER.debug("Setting streams list.");
            if(tournamentGG.getStreams()!=null) {
                streamSelect.getItems().addAll(tournamentGG.getStreams());
            }
            if (eventSelect.getItems().size() > 0){
                eventSelect.getSelectionModel().select(0);
            }
            genText.setDisable(false);
        }catch (IllegalArgumentException e){
            LOGGER.error("Could not connect to Start.gg due to a authorization token issue.");
            LOGGER.catching(e);
            AlertFactory.displayError("Could not connect to Start.gg due to a authorization token issue",
                    ExceptionUtils.getStackTrace(e));
            genText.setDisable(true);

            return;
        }catch (ExecutionException | InterruptedException | NullPointerException e){
            LOGGER.error("An issue occurred when executing query");
            LOGGER.catching(e);
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
                setDisableGeneration(true);
            } else {
                setDisableGeneration(false);
            }
        });
    }


    public void showAuthTokenPage(ActionEvent actionEvent) {
        LOGGER.info("User loaded authorization token instructions' page.");
        String url ="https://developer.start.gg/docs/authentication/";
        if(Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            }catch(URISyntaxException | IOException e ){
                LOGGER.error("Could not open {}", url);
                LOGGER.catching(e);
                AlertFactory.displayError("Could not open the following URL: "+ url, e.getMessage());
            }
        }
    }

    private void setDisableGeneration(boolean disable){
        genStart.setDisable(disable);
        saveLocally.setDisable(disable);
    }
}
