package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.dto.json.read.TournamentRead;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.service.json.JSONReaderService;
import thumbnailgenerator.service.json.JSONWriterService;

@Service
public class TournamentService {
    private final Logger LOGGER = LogManager.getLogger(TournamentService.class);

    private static List<Tournament> tournamentsList;
    private static Tournament selectedTournament;
    private static Tournament selectedEdit;
    @Autowired
    private JSONReaderService jsonReaderService;
    @Autowired
    private JSONWriterService jsonWriterService;

    public void initTournamentsListAndSettings(){
        LOGGER.info("Loading saved tournament list.");
        loadTournamentsList();
        tournamentsList.forEach(tournament ->{
            var textSettings = jsonReaderService.loadTextSettings(tournament.getTournamentId());
            LOGGER.debug("{} -> {}.", tournament.getName(), textSettings);
            for (Game game : Game.values()) {
                tournament
                        .getThumbnailSettingsByGame(game)
                        .setTextSettings(textSettings);
            }
        });
    }

    public void loadTournamentsList(){
        List<TournamentRead> tournamentReadList = jsonReaderService.loadTournament();
        tournamentsList = tournamentReadList.stream().map(t -> new Tournament(t)).collect(Collectors.toList());

    }

    public List<Tournament> getTournamentsList(){
        return tournamentsList;
    }

    public Tournament getSelectedTournament() {
        return selectedTournament;
    }

    public void setSelectedTournament(Tournament selectedTournament) {
        TournamentService.selectedTournament = selectedTournament;
    }

    public Tournament getSelectedEdit() {
        return selectedEdit;
    }

    public void setSelectedEdit(Tournament selectedEdit) {
        TournamentService.selectedEdit = selectedEdit;
    }

    public void updateTournamentsList(Tournament... list) {
        LOGGER.info("Saving updated tournament list.");
        if (list != null){
            for (Tournament tournament:list) {
                LOGGER.info("{} -> {}", tournament.getName(), tournament.toString());
                tournamentsList.add(tournament);
            }
        }

        jsonWriterService.updateTournamentsFile(tournamentsList);
    }

    public void saveNewTournaments(Tournament tournament) {
        LOGGER.info("Saving updated tournament list.");
        if (tournament != null){
            tournamentsList.add(tournament);

        }
        tournamentsList.stream().peek(t -> LOGGER.info("{} -> {}", t.getName(), t.toString()));
        jsonWriterService.updateTournamentsFile(tournamentsList);
        jsonWriterService.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
    }

    public void saveChangesToTournament(Tournament newTournament, Tournament oldVersionTournament) {
        LOGGER.info("Saving updated tournament list.");
        tournamentsList = tournamentsList.stream()
                .map(t -> oldVersionTournament.getTournamentId().equals(t.getTournamentId()) ? newTournament : t )
                .collect(Collectors.toList());
        tournamentsList.stream().peek(t -> LOGGER.info("{} -> {}", t.getName(), t.toString()));
        jsonWriterService.updateTournamentsFile(tournamentsList);
        jsonWriterService.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
    }

    public void deleteTournament(Tournament tournament) {
        boolean delete = AlertFactory.displayConfirmation(
                "Are you sure that you want to eliminate tournament " + tournament.getName());
        if(delete) {
            LOGGER.info("Deleting tournament {}.", tournament.getName());
            tournamentsList.removeIf(t -> tournament.getTournamentId() == t.getTournamentId());
            jsonWriterService.updateTournamentsFile(tournamentsList);
            jsonWriterService.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
        }
    }
}
