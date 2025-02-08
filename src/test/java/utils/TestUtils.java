package utils;

import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.service.TournamentUtils;

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
