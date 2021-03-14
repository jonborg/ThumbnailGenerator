package tournament;

import com.google.gson.reflect.TypeToken;
import file.FileUtils;
import file.json.JSONReader;
import file.json.JSONWriter;
import thumbnail.text.TextSettings;
import ui.controller.ThumbnailGeneratorController;
import ui.factory.alert.AlertFactory;


import java.util.ArrayList;
import java.util.List;

public class TournamentUtils {

    private static List<Tournament> tournamentsList;
    private static Tournament selectedTournament;
    private static Tournament selectedEdit;


    public static void initTournamentsListAndSettings(){
        loadTournamentsList();
        tournamentsList.forEach(tournament ->
            tournament.setTextSettings(TextSettings.loadTextSettings(tournament.getTournamentId())));
    }

    public static void loadTournamentsList(){
        tournamentsList = JSONReader.getJSONArray(FileUtils.getTournamentFile(),
                new TypeToken<ArrayList<Tournament>>(){}.getType());
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
        if (list != null){
            for (Tournament tournament:list) {
                tournamentsList.add(tournament);
            }
        }
        JSONWriter.updateTournamentsFile(tournamentsList);
        ThumbnailGeneratorController.reloadPage();
    }

    public static void updateTournamentsListAndSettings(Tournament... list) {
        if (list != null){
            for (Tournament tournament:list) {
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
            tournamentsList.removeIf(t -> tournament.getTournamentId() == t.getTournamentId());
            JSONWriter.updateTournamentsFile(tournamentsList);
            JSONWriter.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
            ThumbnailGeneratorController.reloadPage();
        }
    }
}
