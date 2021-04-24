package smashgg.query;

import com.github.gpluscb.ggjava.api.GGClient;
import com.github.gpluscb.ggjava.api.RateLimiter;
import com.github.gpluscb.ggjava.entity.object.response.GGResponse;
import com.github.gpluscb.ggjava.entity.object.response.objects.QueryResponse;
import com.google.gson.JsonObject;
import org.codehaus.plexus.util.ExceptionUtils;
import ui.factory.alert.AlertFactory;

import java.util.concurrent.CompletableFuture;

public class QueryUtils {

    private static GGClient client;

    public static void initClient(){
        client = GGClient.builder("a6d3c28a0118d864897a0c6efac41dc1")
                .limiter(RateLimiter.bucketBuilder().tasksPerPeriod(70).period(60000L).build()).build();
    }

    public static void closeClient(){
        if (!client.isShutDown()) {
            client.shutdown();
        }
    }

    public static JsonObject runQuery(String query){
        CompletableFuture<JsonObject> future = client.request(query);
        future.exceptionally(t -> {
            t.printStackTrace();
            return null;
        });
        try{
            return future.get();
        } catch (Exception e) {
            AlertFactory.displayError("An error has occurred", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public static String tournamentDetailsQuery(String tournamentURL){
        String[] urlSplit = tournamentURL.split("/");
        return "query{ " +
                "tournament(slug:  \""+ urlSplit[3] + "/" + urlSplit[4] + "\") {id " +
                "events{ id name " +
                "phases{ id name " +
                "phaseGroups{ nodes{ id displayIdentifier " +
                "}}}}}}";
    }

    public static String getSetsByEvents(int eventId, int currentPage){
        return "query {" +
                "event(id: " + eventId + ") { id name " +
                "sets(page: " + currentPage + " perPage: 20){ " +
                "pageInfo { totalPages }" +
                "nodes {" + matchDetailsSubQuery() +
                "}}}}";
    }

    private static String matchDetailsSubQuery(){
        return "fullRoundText games{ " +
                "selections{ entrant { name } selectionValue selectionType}" +
                "} " +
                "stream{ streamName }";
    }

}
