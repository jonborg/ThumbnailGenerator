package utils;

import thumbnail.text.TextSettings;
import tournament.Tournament;
import tournament.TournamentUtils;

public class TestUtils {

    private TestUtils(){
        throw new UnsupportedOperationException("Utils class");
    }

    public static Tournament getTournament(String tournamentId){
        return TournamentUtils.getTournamentsList()
                .stream()
                .filter(it -> tournamentId.equals(it.getTournamentId()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Tournament "
                                + tournamentId + " not found")
                );
    }
}
