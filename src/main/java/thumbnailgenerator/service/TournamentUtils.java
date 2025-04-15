package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.dto.json.read.TextSettingsRead;
import thumbnailgenerator.dto.json.read.TournamentRead;
import thumbnailgenerator.dto.json.write.TournamentWrite;
import thumbnailgenerator.ui.controller.ThumbnailGeneratorController;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.file.FileUtils;
import thumbnailgenerator.utils.json.JSONReader;
import thumbnailgenerator.utils.json.JSONWriter;

public class TournamentUtils {
    private static final Logger LOGGER = LogManager.getLogger(TournamentUtils.class);

    private static List<Tournament> tournamentsList;
    private static Tournament selectedTournament;
    private static Tournament selectedEdit;

    public static void initTournamentsListAndSettings(){
        LOGGER.info("Loading saved tournament list.");
        loadTournamentsList();
        tournamentsList.forEach(tournament ->{
            var textSettings = JSONReader.loadTextSettings(tournament.getTournamentId());
            LOGGER.debug("{} -> {}.", tournament.getName(), textSettings);
            for (Game game : Game.values()) {
                tournament
                        .getThumbnailSettingsByGame(game)
                        .setTextSettings(textSettings);
            }
        });
    }

    public static void loadTournamentsList(){
        List<TournamentRead> tournamentReadList = JSONReader.getJSONArrayFromFile(FileUtils.getTournamentFile(),
                new TypeToken<ArrayList<TournamentRead>>(){}.getType());
        tournamentsList = tournamentReadList.stream().map(t -> new Tournament(t)).collect(Collectors.toList());
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
    }

    public static void saveNewTournaments(Tournament tournament) {
        LOGGER.info("Saving updated tournament list.");
        if (tournament != null){
            tournamentsList.add(tournament);

        }
        tournamentsList.stream().peek(t -> LOGGER.info("{} -> {}", t.getName(), t.toString()));
        JSONWriter.updateTournamentsFile(tournamentsList);
        JSONWriter.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
    }

    public static void saveChangesToTournament(Tournament newTournament, Tournament oldVersionTournament) {
        LOGGER.info("Saving updated tournament list.");
        tournamentsList = tournamentsList.stream()
                .map(t -> oldVersionTournament.getTournamentId().equals(t.getTournamentId()) ? newTournament : t )
                .collect(Collectors.toList());
        tournamentsList.stream().peek(t -> LOGGER.info("{} -> {}", t.getName(), t.toString()));
        JSONWriter.updateTournamentsFile(tournamentsList);
        JSONWriter.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
    }

    public static void deleteTournament(Tournament tournament) {
        boolean delete = AlertFactory.displayConfirmation(
                "Are you sure that you want to eliminate tournament " + tournament.getName());
        if(delete) {
            LOGGER.info("Deleting tournament {}.", tournament.getName());
            tournamentsList.removeIf(t -> tournament.getTournamentId() == t.getTournamentId());
            JSONWriter.updateTournamentsFile(tournamentsList);
            JSONWriter.updateTextSettingsFile(TextSettings.getAllTextSettings(tournamentsList));
        }
    }
}
