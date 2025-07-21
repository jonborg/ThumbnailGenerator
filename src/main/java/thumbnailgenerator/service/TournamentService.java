package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.FighterArtSettings;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.FileTop8Settings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.TextSettings;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.dto.factory.TournamentFactory;
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
    @Autowired
    private GameEnumService gameEnumService;
    @Autowired
    private TournamentFactory tournamentFactory;

    public void initTournamentsListAndSettings(){
        LOGGER.info("Loading saved tournament list.");
        loadTournamentsList();
        tournamentsList.forEach(tournament ->{
            var textSettings = jsonReaderService.loadTextSettings(tournament.getTournamentId());
            LOGGER.debug("{} -> {}.", tournament.getName(), textSettings);
            for (Game game : Game.values()) {
                getTournamentThumbnailSettingsOrDefault(tournament, game)
                        .setTextSettings(textSettings);
            }
        });
    }

    public void loadTournamentsList(){
        List<TournamentRead> tournamentReadList = jsonReaderService.loadTournament();
        tournamentsList = tournamentReadList.stream().map(t -> tournamentFactory.createTournament(t)).collect(Collectors.toList());
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
                orderGameSettings(tournament);
                tournamentsList.add(tournament);
            }
        }
        jsonWriterService.updateTournamentsFile(tournamentsList);
        jsonWriterService.updateTextSettingsFile(getAllTextSettings());
    }

    public void saveNewTournaments(Tournament tournament) {
        LOGGER.info("Saving updated tournament list.");
        if (tournament != null){
            orderGameSettings(tournament);
            tournamentsList.add(tournament);
        }
        tournamentsList.stream().peek(t -> LOGGER.info("{} -> {}", t.getName(), t.toString()));
        jsonWriterService.updateTournamentsFile(tournamentsList);
        jsonWriterService.updateTextSettingsFile(getAllTextSettings());
    }

    public void saveChangesToTournament(Tournament newTournament, Tournament oldVersionTournament) {
        LOGGER.info("Saving updated tournament list.");
        orderGameSettings(newTournament);
        tournamentsList = tournamentsList.stream()
                .map(t -> oldVersionTournament.getTournamentId().equals(t.getTournamentId()) ? newTournament : t )
                .collect(Collectors.toList());
        tournamentsList.stream().peek(t -> LOGGER.info("{} -> {}", t.getName(), t.toString()));
        jsonWriterService.updateTournamentsFile(tournamentsList);
        jsonWriterService.updateTextSettingsFile(getAllTextSettings());
    }

    public void deleteTournament(Tournament tournament) {
        boolean delete = AlertFactory.displayConfirmation(
                "Are you sure that you want to eliminate tournament " + tournament.getName());
        if(delete) {
            LOGGER.info("Deleting tournament {}.", tournament.getName());
            tournamentsList.removeIf(t -> tournament.getTournamentId() == t.getTournamentId());
            jsonWriterService.updateTournamentsFile(tournamentsList);
            jsonWriterService.updateTextSettingsFile(getAllTextSettings());
        }
    }

    private List<TextSettings> getAllTextSettings() {
        List<TextSettings> textSettingsList = new ArrayList<>();
        tournamentsList.forEach(tournament -> textSettingsList.add(getTournamentThumbnailSettingsOrDefault(tournament, Game.SSBU).getTextSettings()));
        return textSettingsList;
    }

    public FileThumbnailSettings getTournamentThumbnailSettingsOrDefault(Tournament tournament, Game game){
        var result = tournament.getThumbnailSettingsByGame(game);
        if (result == null) {
            List<FighterArtSettings> defaultList = new ArrayList<>();
            var artTypes = gameEnumService.getAllFighterArtTypes(game);
            for (var artType : artTypes){
                defaultList.add(
                        FighterArtSettings.builder()
                                .artType(artType)
                                .fighterImageSettingsPath(gameEnumService.getDefaultFighterArtTypeSettingsFile(game, artType))
                                .build()
                );
            }
            return new FileThumbnailSettings(
                    game,
                    null,
                    null,
                    defaultList,
                    new TextSettings(tournament.getTournamentId())
            );
        } else {
            for (var setting : result.getArtTypeDir()){
                if (setting.getFighterImageSettingsPath() == null) {
                    setting.setFighterImageSettingsPath(
                            gameEnumService.getDefaultFighterArtTypeSettingsFile(game, setting.getArtType())
                    );
                }
            }
            return result;
        }
    }

    public FileTop8Settings getTournamentTop8SettingsOrDefault(Tournament tournament, Game game){
        var result = tournament.getTop8SettingsByGame(game);
        if (result != null) {
            return result;
        }
        List<FighterArtSettings> defaultList = new ArrayList<>();
        var artTypes = gameEnumService.getAllFighterArtTypes(game);
        for (var artType : artTypes){
            defaultList.add(
                    FighterArtSettings.builder()
                            .artType(artType)
                            .fighterImageSettingsPath(null)
                            .build()
            );
        }
        return new FileTop8Settings(
                game,
                null,
                null,
                defaultList,
                null
        );
    }

    private void orderGameSettings(Tournament tournament){
        tournament.getThumbnailSettings().sort(
                Comparator.comparing(s -> s.getGame().ordinal())
        );
        tournament.getTop8Settings().sort(
                Comparator.comparing(s -> s.getGame().ordinal())
        );
    }
}
