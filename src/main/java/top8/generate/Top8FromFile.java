package top8.generate;

import exception.Top8FromFileException;
import fighter.Fighter;
import fighter.Player;
import com.google.gson.reflect.TypeToken;
import exception.FighterImageSettingsNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import exception.ThumbnailFromFileException;
import fighter.FighterArtType;
import file.json.JSONReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnail.image.settings.ImageSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

public class Top8FromFile extends Top8 {
    private static final Logger LOGGER = LogManager.getLogger(Top8FromFile.class);

    private static Tournament selectedTournament;
    private static List<Player> players;
    private static FighterArtType artType;

    public static void generateFromFile(File file,  boolean saveLocally)
        throws Top8FromFileException {

        initMultiGeneration();

        boolean firstLine = true;
        String line = null;
        List<String> invalidLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                LOGGER.info("Reading line: {}", line);
                getParameters(line, firstLine);
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not load file {}.", file.getPath());
            LOGGER.catching(e);
            AlertFactory.displayError("Could not load file: " + file.getPath());
        } catch (IOException e){
            LOGGER.error("Could not parse line {}.", line);
            LOGGER.catching(e);
            AlertFactory.displayError("Could not parse line: "+line);
        } catch (Exception e){
            LOGGER.error("Could not correctly parse provided file");
            LOGGER.catching(e);
            AlertFactory.displayError("Could not correctly parse provided file");
            throw new Top8FromFileException(e);
        }

        try {
            generateTop8FromFile(saveLocally);
        }catch(OnlineImageNotFoundException | FighterImageSettingsNotFoundException e) {
            invalidLines.add(e.getMessage() + " -> " + line);
        }catch(LocalImageNotFoundException e) {
            AlertFactory.displayError(e.getMessage());
            throw new Top8FromFileException();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e){
            AlertFactory.displayError(e.getMessage());
            invalidLines.add("Invalid line -> "+ line);
        }

    }

    private static void initMultiGeneration(){
        selectedTournament= null;
        players = new ArrayList<>();
        artType = FighterArtType.RENDER;
    }

    private static void getParameters(String line, Boolean firstLine){
        if (firstLine){
            var parameters = Arrays.asList(line.split(";"));
            for (Tournament t : TournamentUtils.getTournamentsList()){
                if (t.getTournamentId().equals(parameters.get(0))) {
                    LOGGER.info("Selected tournament {}", t.getName());
                    selectedTournament = t;
                    break;
                }
            };
            if (selectedTournament == null){
                LOGGER.error("Could not find tournament with id {}.", parameters.get(0));
                AlertFactory.displayError("Could not find tournament with id '{}'",parameters.get(0));
                return;
            }
            if(parameters.size()>1
                    && !parameters.get(1).isEmpty()){
                readArtType(parameters.get(1));
            } else {
                artType = FighterArtType.RENDER;
            }
        } else {
            var parameters = Arrays.asList(line.split(";"));
            parameters.replaceAll(String::trim);
            processSlotData(parameters);
        }
    }

    private static void processSlotData(List<String> parameters){
        var fighters = new ArrayList<Fighter>();
        var nFighters = parameters.size()/2;
        for (int i = 0; i<nFighters; i++) {
            fighters.add(new Fighter("", parameters.get(2*i),
                    Integer.parseInt(parameters.get(1+2*i)), false));
        }
        players.add(new Player("", fighters));
    }

    private static void generateTop8FromFile(boolean saveLocally)
            throws IOException, FighterImageSettingsNotFoundException {
        generateTop8(Top8Settings.builder()
                .tournament(selectedTournament)
                .locally(saveLocally)
                .players(players)
                .artType(artType)
                .build());
    }

    private static void readArtType(String art){
        artType = FighterArtType.valueOf(art.toUpperCase());
    }

}
