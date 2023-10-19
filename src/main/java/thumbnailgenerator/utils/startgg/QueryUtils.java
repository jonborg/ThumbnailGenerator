package thumbnailgenerator.utils.startgg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QueryUtils {
    private static final Logger LOGGER = LogManager.getLogger(QueryUtils.class);

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
