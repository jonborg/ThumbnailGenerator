package utils;

import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.service.TournamentService;

public class TestUtils {

    private TestUtils(){
        throw new UnsupportedOperationException("Utils class");
    }

    public static Tournament getTournament(TournamentService tournamentService, String tournamentId){
        return tournamentService.getTournamentsList()
                .stream()
                .filter(it -> tournamentId.equals(it.getTournamentId()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Tournament "
                                + tournamentId + " not found")
                );
    }
}
