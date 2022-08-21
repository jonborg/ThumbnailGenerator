package top8.generate;

import com.google.gson.reflect.TypeToken;
import exception.CharacterImageSettingsNotFoundException;
import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import exception.ThumbnailFromFileException;
import character.Character;
import character.CharacterArtType;
import character.CharacterImage;
import file.json.JSONReader;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thumbnail.generate.ThumbnailSettings;
import thumbnail.image.ImageSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

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

public class Top8FromFile extends Top8 {
    private static final Logger LOGGER = LogManager.getLogger(Top8FromFile.class);

    private static Tournament selectedTournament;
    private static List<Character>
    private static String date;
    private static String edition;
    private static CharacterArtType artType;
    private static ImageSettings imageSettings;

    public static void generateFromFile(File file,  boolean saveLocally)
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
                }catch(OnlineImageNotFoundException | CharacterImageSettingsNotFoundException e) {
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

    private static void initMultiGeneration(){
        selectedTournament= null;
        imageSettings = null;
        date = null;
        edition = null;
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
            edition=parameters.get(1);
            date=parameters.get(2);
            if(parameters.size()>3
                    && !parameters.get(3).isEmpty()){
                readArtType(parameters.get(3));
            } else {
                artType = CharacterArtType.RENDER;
            }
            return;
        }
        LOGGER.info("Loading image settings for tournament {}.", selectedTournament.getName());
        imageSettings = (ImageSettings) JSONReader.getJSONArray(
                selectedTournament.getCharacterImageSettingsFile(artType),
                new TypeToken<ArrayList<ImageSettings>>() {}.getType())
                .get(0);
        var parameters = Arrays.asList(line.split(";"));
        parameters.replaceAll(String::trim);
        processSlotData(parameters);
    }


    private static void generateThumbnail(Boolean saveLocally)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, CharacterImageSettingsNotFoundException {
        var player1 = new Character(parameters.get(0), parameters.get(2), parameters.get(2), Integer.parseInt(parameters.get(4)), false);
        var player2 = new Character(parameters.get(1), parameters.get(3), parameters.get(3), Integer.parseInt(parameters.get(5)), false);
        player1.setFlip(readFlipFile(player1));
        if (imageSettings.isMirrorPlayer2()) {
            player2.setFlip(!readFlipFile(player2));
        } else {
            player2.setFlip(readFlipFile(player2));
        }

        generateAndSaveThumbnail(ThumbnailSettings.builder()
                                                .tournament(selectedTournament)
                                                .imageSettings(imageSettings)
                                                .locally(saveLocally)
                                                .round(parameters.get(6))
                                                .date(date)
                                                .fighters(ThumbnailSettings.createPlayerList(player1, player2))
                                                .artType(artType)
                                                .build());
    }

    private static boolean readFlipFile(Character character) throws
            CharacterImageSettingsNotFoundException {
        boolean result;
        String urlNameOriginal = character.getUrlName();
        CharacterImage.convertToAlternateRender(character);

        result = imageSettings.findFighterImageSettings(character.getUrlName()).isFlip();
        character.setUrlName(urlNameOriginal);
        return result;
    }

    private static void readArtType(String art){
        artType = CharacterArtType.valueOf(art.toUpperCase());
    }

}
