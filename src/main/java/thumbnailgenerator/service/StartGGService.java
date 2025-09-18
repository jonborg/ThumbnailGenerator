package thumbnailgenerator.service;

import com.github.gpluscb.ggjava.api.GGClient;
import com.github.gpluscb.ggjava.api.RateLimiter;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.startgg.match.GameGG;
import thumbnailgenerator.dto.startgg.match.SelectionGG;
import thumbnailgenerator.dto.startgg.match.SetGG;
import thumbnailgenerator.dto.startgg.match.SetNodeGG;
import thumbnailgenerator.dto.startgg.search.SearchGamesGG;
import thumbnailgenerator.enums.games.ssbm.SmashMeleeEnum;
import thumbnailgenerator.service.games.GameEnumService;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.service.json.JSONReaderService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class StartGGService {

    private static final Logger LOGGER = LogManager.getLogger(StartGGService.class);
    private GGClient client;
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private GameEnumService gameEnumService;
    @Autowired
    private JSONReaderService jsonReaderService;

    private static Game eventGame;

    public void initClient(String authToken){
        client = GGClient.builder(authToken)
                .limiter(RateLimiter.bucketBuilder().tasksPerPeriod(70).period(60000L).build()).build();
    }

    public void closeClient(){
        if (client != null && !client.isShutDown()) {
            client.shutdown();
        }
    }

    public JsonObject runQuery(String query) throws ExecutionException, InterruptedException {
        CompletableFuture<JsonObject> future = client.request(query);
        future.exceptionally(t -> {
            LOGGER.error("An issue has occurred when trying to use Start.gg API");
            LOGGER.catching(t);
            AlertFactory.displayError("An issue has occurred when trying to use Start.gg API",
                    ExceptionUtils.getStackTrace(t));
            return null;
        });

        return future.get();
    }

    public JsonObject queryStartGGForGames(SearchGamesGG searchGamesGG)
            throws ExecutionException, InterruptedException {
        LOGGER.debug("Running query -> {}", searchGamesGG.getQuery());
        JsonObject result = runQuery(searchGamesGG.getQuery());
        LOGGER.debug("Result -> {}", result.toString());
        return result;
    }

    public int getTotalPages(SearchGamesGG searchGamesGG, JsonObject result){
        return result.getAsJsonObject("data")
                .getAsJsonObject(searchGamesGG.getSearchMode())
                .getAsJsonObject("sets")
                .getAsJsonObject("pageInfo")
                .getAsJsonPrimitive("totalPages")
                .getAsInt();
    }

    public String generateTournamentData(SearchGamesGG searchGamesGG) {
        var tournamentData = new StringBuilder();
        eventGame = findGameByStartGGId(searchGamesGG.getGameId());
        var defaultArtType = gameEnumService.getDefaultArtType(eventGame).getEnumName();

        //Append first line of script regarding tournament data
        tournamentData.append(tournamentService.getSelectedTournament().getTournamentId())
                .append(";")
                .append(eventGame)
                .append(";")
                .append(searchGamesGG.getEventName())
                .append(";")
                .append(defaultArtType)
                .append(System.lineSeparator());

        return tournamentData.toString();
    }

    public String readSetsFromSmashGGPage(SearchGamesGG searchGamesGG, JsonObject queryResponse, boolean isMultipleCharacters)
            throws ExecutionException, InterruptedException {
        var foundSets = new StringBuilder();
        SetGG set = (SetGG) jsonReaderService
                .getJSONObject(queryResponse.getAsJsonObject("data").getAsJsonObject(searchGamesGG.getSearchMode())
                        .getAsJsonObject("sets").toString(), new TypeToken<SetGG>() {}.getType());

        set.getSetNodes().forEach(setNodeGG -> {
            if(setNodeGG.hasStream()) {
                var setNode = setNodeGGToString(setNodeGG, isMultipleCharacters);
                if(searchGamesGG.getStream() != null
                        && !searchGamesGG.getStream().isNull()) {
                    if (searchGamesGG.getStream().getStreamName().equals(setNodeGG.getStreamName())){
                        LOGGER.info("Filtering sets by stream {}.", searchGamesGG.getStream().getStreamName());
                        appendSet(setNode, foundSets);
                    }
                } else {
                    appendSet(setNode, foundSets);
                }
            }
        });
        return foundSets.toString();
    }

    private void appendSet(String setNode, StringBuilder foundSets){
        LOGGER.debug("Found set -> {}", setNode);
        foundSets.append(setNode);
        foundSets.append(System.lineSeparator());
    }

    private Game findGameByStartGGId(int startGGId) {
        return Arrays.stream(Game.values())
                .filter(g -> g.getStartGGId() == startGGId)
                .findFirst()
                .orElse(null);
    }

    public String getMostUsedCharacter(List<GameGG> games, String entrantName, boolean isMultipleCharacters){
        HashMap<Integer,Integer> charSel = new HashMap<>();
        String defaultCharacterCode = "random";
        if (games == null) {
            return defaultCharacterCode;
        }
        for (GameGG gameGG :games) {
            if (gameGG != null && gameGG.getSelections() != null) {
                for (SelectionGG selectionGG : gameGG.getSelections()) {
                    if (selectionGG.getEntrant().getName().equals(entrantName)) {
                        int character = selectionGG.getSelectionValue();
                        if (charSel.containsKey(character)) {
                            charSel.put(character, charSel.get(character) + 1);
                        } else {
                            charSel.put(character, 1);
                        }
                        break;
                    }
                }
            }
        }
        if (charSel.isEmpty()) {
            return defaultCharacterCode;
        }
        List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<>(charSel.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        if (isMultipleCharacters && sortedEntries.size() > 1) {
            return findCodeByStartggId(sortedEntries.get(0).getKey()) + "," + findCodeByStartggId(sortedEntries.get(1).getKey());
        } else {
            return findCodeByStartggId(sortedEntries.get(0).getKey());
        }
    }

    public String setNodeGGToString(SetNodeGG setNodeGG, boolean isMultipleCharacters){
        var games = setNodeGG.getGames();
        var roundName = setNodeGG.getRoundName();
        String player1 = setNodeGG.getEntrant(0).getName();
        String player2 = setNodeGG.getEntrant(1).getName();

        String player1NoTeam = setNodeGG.getEntrateNameWithNoTeam(player1);
        String player2NoTeam = setNodeGG.getEntrateNameWithNoTeam(player2);

        String player1Characters = getMostUsedCharacter(games, player1, isMultipleCharacters);
        String player2Characters = getMostUsedCharacter(games, player2, isMultipleCharacters);

        String player1Alts = player1Characters.contains(",") ? "1,1" : "1";
        String player2Alts = player2Characters.contains(",") ? "1,1" : "1";

        return player1NoTeam + ";" + player2NoTeam + ";"
                + player1Characters + ";" + player2Characters + ";"
                + player1Alts + ";" + player2Alts + ";"
                + roundName;
    }

    private String findCodeByStartggId(int mostUsedCharacter) {
        //Sheik / Zelda
        if (mostUsedCharacter == 628) {
            return SmashMeleeEnum.SHEIK.getCode();
        }
        var result = gameEnumService.findCharacterCodeByStartGGId(eventGame, mostUsedCharacter);
        if (result == null) {
            LOGGER.warn("Start.gg character id " + mostUsedCharacter
                    + " is unknown for " + eventGame.getName());
            return "random";
        }
        return result;
    }
}
