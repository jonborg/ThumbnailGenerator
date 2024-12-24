package thumbnailgenerator.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.service.TournamentUtils;
import thumbnailgenerator.ui.buttons.TournamentButton;

@Controller
public class TournamentsController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(TournamentsController.class);

    @FXML
    private Label tournamentsLabel;
    @FXML
    private HBox tournamentsBox;

    private static ToggleGroup tournamentsGroup;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTournamentList();
        initListeners();
    }

    private void initTournamentList(){
        List<TournamentButton> tournamentsButtons = new ArrayList<>();
        tournamentsGroup = new ToggleGroup();
        if ( getTournamentsList() == null || getTournamentsList().isEmpty()){
            return;
        }
        getTournamentsList().forEach(tournament -> {
            TournamentButton button = new TournamentButton(tournament);
            button.setToggleGroup(tournamentsGroup);
            button.setId(tournament.getTournamentId());
            tournamentsButtons.add(button);
            tournamentsBox.getChildren().add(button);
        });
    }

    private void initListeners(){
        String tournamentsLabelTitle = "Tournament:";
        tournamentsGroup.selectedToggleProperty().addListener((obs,oldToggle,newToggle)->{
            if (newToggle == null){
                tournamentsLabel.setText(tournamentsLabelTitle);
                setSelectedTournament(null);
            }else{
                for(Node node :  tournamentsBox.getChildren()){
                    if (node instanceof TournamentButton) {
                        TournamentButton tournamentButton = (TournamentButton) node;
                        if (tournamentButton.isSelected()) {
                            LOGGER.info("User selected tournament {}", tournamentButton.getName());
                            setSelectedTournament(tournamentButton.getTournament());
                            tournamentsLabel.setText(tournamentsLabelTitle + " " + tournamentButton.getName());
                        }
                    }
                }
            }
        });
    }

    private static List<Tournament> getTournamentsList() {
        return TournamentUtils.getTournamentsList();
    }
    private static void setSelectedTournament(Tournament tournament) {
        TournamentUtils.setSelectedTournament(tournament);
    }
}
