package thumbnail.generate;

import com.google.gson.reflect.TypeToken;
import exception.*;
import fighter.Fighter;
import fighter.FighterImage;
import file.json.JSONReader;
import thumbnail.image.ImageSettings;
import tournament.Tournament;
import tournament.TournamentUtils;
import ui.factory.alert.AlertFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThumbnailFromFile extends Thumbnail {

    private static String date = null;
    private static List<String> parameters;
    private static Tournament selectedTournament;
    private static ImageSettings imageSettings;

    public static void generateFromFile(File file, boolean saveLocally)
        throws ThumbnailFromFileException, FontNotFoundException{

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
            AlertFactory.displayError("Could not file: " + file.getPath());
        }catch (IOException e){
            AlertFactory.displayError("Could not correctly parse line: "+line);
        }

        if (!invalidLines.isEmpty()){
            String details = "";
            for (String  l :invalidLines){
                details += l + System.lineSeparator() + System.lineSeparator();
            }
            AlertFactory.displayError("Thumbnails could not be generated from these lines: ", details);
            throw new ThumbnailFromFileException();
        }
    }

    public static void generateFromSmashGG(String commands) throws ThumbnailFromFileException, FontNotFoundException {

        initMultiGeneration();

        boolean saveLocally=false;

        boolean firstLine = true;
        List<String> invalidLines = new ArrayList<>();

        String[] listCommands = commands.split("\n");
        for (String command : listCommands){
            if (command.isEmpty()){
                continue;
            }
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
            for (String  l :invalidLines){
                details += l + System.lineSeparator() + System.lineSeparator();
            }
            AlertFactory.displayError("Thumbnails could not be generated from these lines: ", details);
            throw new ThumbnailFromFileException();
        }
    }

    private static void initMultiGeneration(){
        parameters = new ArrayList<>();
        selectedTournament= null;
        imageSettings = null;
        date = null;
    }

    private static void getParameters(String line, Boolean firstLine){
        if (firstLine){
            parameters = Arrays.asList(line.split(";"));
            for (Tournament t : TournamentUtils.getTournamentsList()){
                if (t.getTournamentId().equals(parameters.get(0))) {
                    selectedTournament = t;
                    break;
                }
            };
            if (selectedTournament == null){
                AlertFactory.displayError("Could not find tournament with id '{}'",parameters.get(0));
                return;
            }
            date=parameters.get(1);
            return;
        }
        imageSettings = (ImageSettings) JSONReader.getJSONArray(
                selectedTournament.getFighterImageSettingsFile(),
                new TypeToken<ArrayList<ImageSettings>>() {}.getType())
                .get(0);
        parameters = Arrays.asList(line.split(";"));
        parameters.replaceAll(String::trim);
    }


    private static void generateThumbnail(Boolean saveLocally)
            throws LocalImageNotFoundException, OnlineImageNotFoundException,
            FontNotFoundException, FighterImageSettingsNotFoundException {
        Fighter player1 = new Fighter(parameters.get(0), parameters.get(2), parameters.get(2), Integer.parseInt(parameters.get(4)), false);
        Fighter player2 = new Fighter(parameters.get(1), parameters.get(3), parameters.get(3), Integer.parseInt(parameters.get(5)), false);
        player1.setFlip(readFlipFile(player1));
        if (imageSettings.isMirrorPlayer2()) {
            player2.setFlip(!readFlipFile(player2));
        } else {
            player2.setFlip(readFlipFile(player2));
        }
        generateAndSaveThumbnail(selectedTournament, imageSettings, saveLocally, parameters.get(6), date, player1, player2);
    }

    private static boolean readFlipFile(Fighter fighter) throws FighterImageSettingsNotFoundException {
        boolean result;
        String urlNameOriginal = fighter.getUrlName();
        FighterImage.convertToAlternateRender(fighter);

        result = imageSettings.findFighterImageSettings(fighter.getUrlName()).isFlip();
        fighter.setUrlName(urlNameOriginal);
        return result;
    }


}
