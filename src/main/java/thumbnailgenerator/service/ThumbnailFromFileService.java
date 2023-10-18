package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.FighterImage;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.dto.Tournament;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.exception.FighterImageSettingsNotFoundException;
import thumbnailgenerator.exception.FontNotFoundException;
import thumbnailgenerator.exception.LocalImageNotFoundException;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.exception.ThumbnailFromFileException;
import thumbnailgenerator.ui.controller.ThumbnailGeneratorController;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.utils.json.JSONReader;

@Service
public class ThumbnailFromFileService extends ThumbnailService {
    private static final Logger LOGGER = LogManager.getLogger(
            ThumbnailGeneratorController.class);

    private static Tournament selectedTournament;
    private static String date = null;
    private static SmashUltimateFighterArtType artType;

    private static List<String> parameters;
    private static ImageSettings imageSettings;

    private @Autowired ThumbnailService thumbnailService;

    public void generateFromFile(File file, boolean saveLocally)
        throws ThumbnailFromFileException, FontNotFoundException {

        initMultiGeneration();

        boolean firstLine = true;
        String line = null;
        List<String> invalidLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()){
                    continue;
                }
                LOGGER.info("Reading line: {}", line);
                getParameters(line, firstLine);
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                   generateThumbnail(saveLocally);
                }catch(OnlineImageNotFoundException | FighterImageSettingsNotFoundException e) {
                    invalidLines.add(e.getMessage() + " -> " + line);
                }catch(LocalImageNotFoundException e) {
                    AlertFactory.displayError(e.getMessage());
                    throw new ThumbnailFromFileException();
                }catch(FontNotFoundException e){
                    throw e;
                }catch (Exception e){
                    invalidLines.add("Invalid line -> "+ line);
                }
            }
        }catch (FileNotFoundException e){
            LOGGER.error("Could not load file {}.", file.getPath());
            LOGGER.catching(e);
            AlertFactory.displayError("Could not load file: " + file.getPath());
        }catch (IOException e){
            LOGGER.error("Could not parse line {}.", line);
            LOGGER.catching(e);
            AlertFactory.displayError("Could not parse line: "+line);
        }catch (Exception e){
            LOGGER.error("Could not correctly parse provided file");
            LOGGER.catching(e);
            AlertFactory.displayError("Could not correctly parse provided file");
            throw new ThumbnailFromFileException(e);
        }

        if (!invalidLines.isEmpty()){
            String details = "";
            LOGGER.error("Thumbnails could not be generated from these lines:");
            for (String  l :invalidLines){
                LOGGER.error(l);
                details += l + System.lineSeparator() + System.lineSeparator();
            }
            AlertFactory.displayError("Thumbnails could not be generated from these lines: ", details);
            throw new ThumbnailFromFileException();
        }
    }

    public void generateFromSmashGG(String commands, boolean saveLocally)
            throws ThumbnailFromFileException, FontNotFoundException {

        initMultiGeneration();

        boolean firstLine = true;
        List<String> invalidLines = new ArrayList<>();

        String[] listCommands = commands.split("\n");
        for (String command : listCommands){
            if (command.isEmpty()){
                continue;
            }
            LOGGER.info("Reading command: {}", command);
            getParameters(command, firstLine);
            if (firstLine) {
                firstLine = false;
                continue;
            }
            try {
                generateThumbnail(saveLocally);
            }catch(OnlineImageNotFoundException | FighterImageSettingsNotFoundException e) {
                invalidLines.add(e.getMessage() + " -> " + command);
            }catch(LocalImageNotFoundException e) {
                AlertFactory.displayError(e.getMessage());
                throw new ThumbnailFromFileException();
            }catch(FontNotFoundException e){
                throw e;
            }catch (Exception e){
                invalidLines.add("Invalid line -> "+ command);
            }
        }
        if (!invalidLines.isEmpty()){
            String details = "";
            LOGGER.error("Thumbnails could not be generated from these lines:");
            for (String  l :invalidLines){
                LOGGER.error(l);
                details += l + System.lineSeparator() + System.lineSeparator();
            }
            AlertFactory.displayError("Thumbnails could not be generated from these lines: ", details);
            throw new ThumbnailFromFileException();
        }
    }

    private void initMultiGeneration(){
        parameters = new ArrayList<>();
        selectedTournament= null;
        imageSettings = null;
        date = null;
    }

    private void getParameters(String line, Boolean firstLine){
        if (firstLine){
            parameters = Arrays.asList(line.split(";"));
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
            date=parameters.get(1);
            if(parameters.size()>2
                    && !parameters.get(2).isEmpty()){
                readArtType(parameters.get(2));
            } else {
                artType = SmashUltimateFighterArtType.RENDER;
            }
            return;
        }
        LOGGER.info("Loading image settings for tournament {}.", selectedTournament.getName());
        imageSettings = (ImageSettings) JSONReader.getJSONArrayFromFile(
                selectedTournament.getThumbnailSettings()
                        .getFighterImageSettingsFile(artType),
                new TypeToken<ArrayList<ImageSettings>>() {}.getType())
                .get(0);
        parameters = Arrays.asList(line.split(";"));
        parameters.replaceAll(String::trim);
    }


    private void generateThumbnail(Boolean saveLocally)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException,
            MalformedURLException {

        var player1 = new Player(parameters.get(0), parameters.get(2), parameters.get(2), Integer.parseInt(parameters.get(4)), false);
        var player2 = new Player(parameters.get(1), parameters.get(3), parameters.get(3), Integer.parseInt(parameters.get(5)), false);
        player1.setFighterFlip(0, readFlipFile(player1.getFighter(0)));
        if (imageSettings.isMirrorPlayer2()) {
            player2.setFighterFlip(0, !readFlipFile(player2.getFighter(0)));
        } else {
            player2.setFighterFlip(0, readFlipFile(player2.getFighter(0)));
        }

        generateAndSaveThumbnail(Thumbnail.builder()
                                                .tournament(selectedTournament)
                                                .game(Game.SMASH_ULTIMATE)
                                                .imageSettings(imageSettings)
                                                .locally(saveLocally)
                                                .round(parameters.get(6))
                                                .date(date)
                                                .players(Thumbnail.createPlayerList(player1, player2))
                                                .artType(artType)
                                                .build());
    }

    private static boolean readFlipFile(Fighter fighter) throws FighterImageSettingsNotFoundException {
        boolean result;
        String urlNameOriginal = fighter.getUrlName();
        FighterImage.convertToAlternateRender(fighter);

        result = imageSettings.findFighterImageSettings(fighter.getUrlName()).isFlip();
        fighter.setUrlName(urlNameOriginal);
        return result;
    }

    private static void readArtType(String art){
        artType = SmashUltimateFighterArtType.valueOf(art.toUpperCase());
    }

}
