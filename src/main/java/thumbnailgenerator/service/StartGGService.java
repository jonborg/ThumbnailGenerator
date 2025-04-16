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
import thumbnailgenerator.dto.startgg.match.SetGG;
import thumbnailgenerator.dto.startgg.search.SearchGamesGG;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.enums.StartGGEnumUtils;
import thumbnailgenerator.service.json.JSONReaderService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class StartGGService {

    private static final Logger LOGGER = LogManager.getLogger(StartGGService.class);
    private GGClient client;
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private JSONReaderService jsonReaderService;

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

    public String readSetsFromSmashGGPage(SearchGamesGG searchGamesGG, int totalPages)
            throws ExecutionException, InterruptedException {
        var foundSets = new StringBuffer();
        LOGGER.debug("Running query -> {}", searchGamesGG.getQuery());
        JsonObject result = runQuery(searchGamesGG.getQuery());
        LOGGER.debug("Result -> {}", result.toString());
        Game eventGame = StartGGEnumUtils.findGameByStartGGId(searchGamesGG.getGameId());

        if (totalPages < 0){
            totalPages = result.getAsJsonObject("data").getAsJsonObject(searchGamesGG.getSearchMode()).getAsJsonObject("sets")
                    .getAsJsonObject("pageInfo").getAsJsonPrimitive("totalPages").getAsInt();
            foundSets.append(tournamentService.getSelectedTournament().getTournamentId()
                    + ";" + eventGame +";" + searchGamesGG.getEventName() + ";RENDER"+ System.lineSeparator());
        }
        SetGG set = (SetGG) jsonReaderService
                .getJSONObject(result.getAsJsonObject("data").getAsJsonObject(searchGamesGG.getSearchMode())
                        .getAsJsonObject("sets").toString(), new TypeToken<SetGG>() {}.getType());
        set.getSetNodes().forEach(setNodeGG -> {
            if(setNodeGG.hasStream()) {
                if(searchGamesGG.getStream() == null || searchGamesGG.getStream().isNull()) {
                    LOGGER.debug("Found set -> {}", setNodeGG.toString());
                    foundSets.append(setNodeGG.toString()+System.lineSeparator());
                } else {
                    LOGGER.info("Filtering sets by stream {}.", searchGamesGG.getStream().getStreamName());
                    if(searchGamesGG.getStream().getStreamName().equals(setNodeGG.getStreamName())){
                        LOGGER.debug("Found set -> {}", setNodeGG.toString());
                        foundSets.append(setNodeGG.toString()+System.lineSeparator());
                    }
                }
            }
        });
        return foundSets.toString();
    }
}
