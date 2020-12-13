package json;


import com.google.gson.*;
import ui.factory.alert.AlertFactory;
import ui.tournament.Tournament;
import ui.tournament.TournamentButton;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONWriter {
    private static AlertFactory alertFactory = new AlertFactory();
    private static String tournamentFile = "settings/tournaments/tournaments.json";



    public static void updateTournamentsFile(List<Tournament> list){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(tournamentFile)) {
            String json = gson.toJson(list);
            System.out.println(json);
            writer.write(json);
        } catch (FileNotFoundException e) {
            alertFactory.displayError("FileNotFoundException", e.getStackTrace().toString());
        } catch (IOException e) {
            alertFactory.displayError("IOException", e.getStackTrace().toString());
        }
    }
}
