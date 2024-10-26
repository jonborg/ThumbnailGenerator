package thumbnailgenerator.ui.controller;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.awt.Desktop;
import java.io.FileNotFoundException;
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
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.dto.startgg.match.StreamGG;
import thumbnailgenerator.dto.startgg.search.SearchGamesGG;
import thumbnailgenerator.dto.startgg.tournament.EventGG;
import thumbnailgenerator.dto.startgg.tournament.PhaseGG;
import thumbnailgenerator.dto.startgg.tournament.PhaseGroupNodeGG;
import thumbnailgenerator.dto.startgg.tournament.TournamentGG;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.exception.FontNotFoundException;
import thumbnailgenerator.exception.ThumbnailFromFileException;
import thumbnailgenerator.service.StartGGService;
import thumbnailgenerator.utils.startgg.QueryUtils;
import thumbnailgenerator.service.ThumbnailService;
import thumbnailgenerator.service.TournamentUtils;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.json.JSONReader;

@Controller
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
    private @Autowired ThumbnailService thumbnailService;
    private @Autowired StartGGService startGGService;

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
            startGGService.initClient(authToken.getText());
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
        var eventName = eventSelect.getSelectionModel().getSelectedItem().getName();
        StringBuilder result = new StringBuilder();

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
                var selectedStream = streamSelect.getSelectionModel().getSelectedItem();
                var searchGamesGG = SearchGamesGG.builder()
                                                 .query(query)
                                                 .eventName(eventName)
                                                 .searchMode(mainBody)
                                                 .stream(selectedStream)
                                                 .build();
                result.append(startGGService
                        .readSetsFromSmashGGPage(searchGamesGG, totalPages));
            }while(readPages<totalPages);
            foundSets.setText(result.toString());
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
            startGGService.closeClient();
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
            thumbnailService
                    .generateFromSmashGG(foundSets.getText(), saveLocally.isSelected());
            LOGGER.info("Thumbnails were successfully generated and saved.");
            AlertFactory.displayInfo("Thumbnails were successfully generated and saved!");
        }catch(ThumbnailFromFileException e){
            //AlertFactory already thrown inside ThumbnailFromFile.generateFromSmashGG
        }catch(FontNotFoundException e){
            AlertFactory.displayError(e.getMessage());
        } catch (FileNotFoundException | FighterImageSettingsNotFoundException e) {
            e.printStackTrace();
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
            startGGService.initClient(authToken.getText());
            LOGGER.info("Loading tournament details from Start.gg.");
            LOGGER.debug("Running query -> {}", QueryUtils.tournamentDetailsQuery(tournamentURL.getText()));
            JsonObject result = startGGService.runQuery(QueryUtils.tournamentDetailsQuery(tournamentURL.getText()));
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
            startGGService.closeClient();
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
