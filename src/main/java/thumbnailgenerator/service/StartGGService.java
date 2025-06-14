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
import thumbnailgenerator.enums.SmashMeleeEnum;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.service.json.JSONReaderService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public String readSetsFromSmashGGPage(SearchGamesGG searchGamesGG, int totalPages, boolean isMultipleCharacters)
            throws ExecutionException, InterruptedException {
        var foundSets = new StringBuffer();
        LOGGER.debug("Running query -> {}", searchGamesGG.getQuery());
        JsonObject result = runQuery(searchGamesGG.getQuery());
        LOGGER.debug("Result -> {}", result.toString());
        eventGame = findGameByStartGGId(searchGamesGG.getGameId());
        var defaultArtType = gameEnumService.getDefaultArtType(eventGame).getEnumName();
        if (totalPages < 0){
            totalPages = result.getAsJsonObject("data").getAsJsonObject(searchGamesGG.getSearchMode()).getAsJsonObject("sets")
                    .getAsJsonObject("pageInfo").getAsJsonPrimitive("totalPages").getAsInt();
            foundSets.append(tournamentService.getSelectedTournament().getTournamentId()
                    + ";" + eventGame +";" + searchGamesGG.getEventName() + ";" + defaultArtType + System.lineSeparator());
        }
        SetGG set = (SetGG) jsonReaderService
                .getJSONObject(result.getAsJsonObject("data").getAsJsonObject(searchGamesGG.getSearchMode())
                        .getAsJsonObject("sets").toString(), new TypeToken<SetGG>() {}.getType());
        set.getSetNodes().forEach(setNodeGG -> {
            if(setNodeGG.hasStream()) {
                var setNode = setNodeGGToString(setNodeGG, isMultipleCharacters);
                if(searchGamesGG.getStream() == null || searchGamesGG.getStream().isNull()) {
                    LOGGER.debug("Found set -> {}", setNode);
                    foundSets.append(setNode+System.lineSeparator());
                } else {
                    LOGGER.info("Filtering sets by stream {}.", searchGamesGG.getStream().getStreamName());
                    if(searchGamesGG.getStream().getStreamName().equals(setNodeGG.getStreamName())){
                        LOGGER.debug("Found set -> {}", setNode);
                        foundSets.append(setNode+System.lineSeparator());
                    }
                }
            }
        });
        return foundSets.toString();
    }

    private Game findGameByStartGGId(int startGGId) {
        return Arrays.stream(Game.values())
                .filter(g -> g.getStartGGId() == startGGId)
                .findFirst()
                .get();
    }

    public String getMostUsedCharacter(List<GameGG> games, String entrantName, boolean isMultipleCharacters){
        HashMap<Integer,Integer> charSel = new HashMap<>();
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
            return "random";
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

        String characters1;
        String characters2;

        if (games == null){
            characters1 = "random";
            characters2 = "random";
        }else{
            characters1 = getMostUsedCharacter(games, player1, isMultipleCharacters);
            characters2 = getMostUsedCharacter(games, player2, isMultipleCharacters);
        }

        String alt1 = characters1.contains(",") ? "1,1" : "1";
        String alt2 = characters2.contains(",") ? "1,1" : "1";

        return player1NoTeam + ";" + player2NoTeam + ";"
                + characters1 + ";" + characters2 + ";"
                + alt1 + ";" + alt2 + ";"
                + roundName;
    }

    private String findCodeByStartggId(int mostUsedCharacter) {
        //Sheik / Zelda
        if (mostUsedCharacter == 628) {
            return SmashMeleeEnum.SHEIK.getCode();
        }
        return gameEnumService.findCharacterCodeByStartGGId(eventGame, mostUsedCharacter);
    }
}
