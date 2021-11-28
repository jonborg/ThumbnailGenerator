package smashgg.query;

import com.github.gpluscb.ggjava.api.GGClient;
import com.github.gpluscb.ggjava.api.RateLimiter;
import com.google.gson.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import ui.factory.alert.AlertFactory;

public class QueryUtils {
    private static final Logger LOGGER = LogManager.getLogger(QueryUtils.class);

    private static GGClient client;

    public static void initClient(String authToken){
        client = GGClient.builder(authToken)
                .limiter(RateLimiter.bucketBuilder().tasksPerPeriod(70).period(60000L).build()).build();
    }

    public static void closeClient(){
        if (client != null && !client.isShutDown()) {
            client.shutdown();
        }
    }

    public static JsonObject runQuery(String query) throws ExecutionException, InterruptedException {
        CompletableFuture<JsonObject> future = client.request(query);
        future.exceptionally(t -> {
            LOGGER.error("An issue has occurred when trying to use Smash.gg API");
            LOGGER.catching(t);
            AlertFactory.displayError("An issue has occurred when trying to use Smash.gg API",
                    ExceptionUtils.getStackTrace(t));
            return null;
        });

        return future.get();
    }

    public static String tournamentDetailsQuery(String tournamentURL){
        String[] urlSplit = tournamentURL.split("/");
        return "query{ " +
                "tournament(slug:  \""+ urlSplit[3] + "/" + urlSplit[4] + "\") {id " +
                "streams{ streamName }" +
                "events{ id name " +
                "phases{ id name " +
                "phaseGroups{ nodes{ id displayIdentifier " +
                "}}}}}}";
    }

    public static String getSetsByEvent(int eventId, int currentPage){
        return "query {" +
                "event(id: " + eventId + ") { id name " +
                setPagesAndMatchesSubQuery(currentPage) +
                "}}";
    }

    public static String getSetsByPhase(int phaseId, int currentPage){
        return "query {" +
                "phase(id: " + phaseId + ") { id name " +
                setPagesAndMatchesSubQuery(currentPage) +
                "}}";
    }

    public static String getSetsByPhaseGroup(int phaseGroupId, int currentPage){
        return "query {" +
                "phaseGroup(id: " + phaseGroupId + ") { id displayIdentifier " +
                setPagesAndMatchesSubQuery(currentPage) +
                "}}";
    }

    private static String setPagesAndMatchesSubQuery(int currentPage){
        return "sets(page: " + currentPage + " perPage: 20 sortType: RECENT){ " +
                "pageInfo { totalPages } " +
                matchDetailsSubQuery() +
                "}";
    }


    private static String matchDetailsSubQuery(){
        return "nodes { fullRoundText " +
                "slots{ entrant{ name } } " +
                "games{ selections{ entrant { name } selectionValue selectionType} } " +
                "stream{ streamName } " +
                "}";
    }

}
