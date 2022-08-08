package tournament;

import com.google.gson.reflect.TypeToken;
import file.FileUtils;
import file.json.JSONReader;
import file.json.JSONWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnail.text.TextSettings;
import ui.controller.ThumbnailGeneratorController;
import ui.factory.alert.AlertFactory;


public class TournamentUtils {
    private static final Logger LOGGER = LogManager.getLogger(TournamentUtils.class);

    private static List<Tournament> tournamentsList;
    private static Tournament selectedTournament;
    private static Tournament selectedEdit;


    public static void initTournamentsListAndSettings(){
        LOGGER.info("Loading saved tournament list.");
        loadTournamentsList();
        tournamentsList.forEach(tournament ->{
            var textSettings = TextSettings.loadTextSettings(tournament.getTournamentId());
            LOGGER.debug("{} -> {}.", tournament.getName(), textSettings);
            tournament.setTextSettings(textSettings);
        });
    }

    public static void loadTournamentsList(){
        tournamentsList = JSONReader.getJSONArray(FileUtils.getTournamentFile(),
                new TypeToken<ArrayList<Tournament>>(){}.getType());
        tournamentsList.size();
    }

    public static List<Tournament> getTournamentsList(){
        return tournamentsList;
    }

    public static Tournament getSelectedTournament() {
        return selectedTournament;
    }

    public static void setSelectedTournament(Tournament selectedTournament) {
        TournamentUtils.selectedTournament = selectedTournament;
    }

    public static Tournament getSelectedEdit() {
        return selectedEdit;
    }

    public static void setSelectedEdit(Tournament selectedEdit) {
        TournamentUtils.selectedEdit = selectedEdit;
    }

    public static void updateTournamentsList(Tournament... list) {
        LOGGER.info("Saving updated tournament list.");
        if (list != null){
            for (Tournament tournament:list) {
                LOGGER.info("{} -> {}", tournament.getName(), tournament.toString());
                tournamentsList.add(tournament);
            }
        }
        JSONWriter.updateTournamentsFile(tournamentsList);
        ThumbnailGeneratorController.reloadPage();
    }

    public static void updateTournamentsListAndSettings(Tournament... list) {
        LOGGER.info("Saving updated tournament list.");
        if (list != null){
            for (Tournament tournament:list) {
                LOGGER.info("{} -> {}", tournament.getName(), tournament.toString());
                tournamentsList.add(tournament);
            }
        }
        JSONWriter.updateTournamentsFile(tournamentsList);
        JSONWriter.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
        ThumbnailGeneratorController.reloadPage();
    }

    public static void deleteTournament(Tournament tournament) {
        boolean delete = AlertFactory.displayConfirmation(
                "Are you sure that you want to eliminate tournament " + tournament.getName());
        if(delete) {
            LOGGER.info("Deleting tournament {}.", tournament.getName());
            tournamentsList.removeIf(t -> tournament.getTournamentId() == t.getTournamentId());
            JSONWriter.updateTournamentsFile(tournamentsList);
            JSONWriter.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
            ThumbnailGeneratorController.reloadPage();
        }
    }
}
