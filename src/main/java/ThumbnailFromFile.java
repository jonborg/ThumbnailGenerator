import exception.FontNotFoundException;
import exception.LocalImageNotFoundException;
import exception.OnlineImageNotFoundException;
import exception.ThumbnailFromFileException;
import fighter.Fighter;
import javafx.scene.control.Alert;
import ui.factory.alert.AlertFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThumbnailFromFile extends Thumbnail {
    AlertFactory alertFactory = new AlertFactory();

    public void generateFromFile(File file, boolean saveLocally)
        throws ThumbnailFromFileException, FontNotFoundException{
        boolean firstLine = true;
        String foreground = null;
        String date = null;
        String line = null;

        List<String> invalidLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()){
                    continue;
                }
                List<String> parameters;
                if (firstLine){
                    parameters = Arrays.asList(line.split(";"));
                    if ("tdlx".equalsIgnoreCase(parameters.get(0))){
                        foreground = "foregroundLx.png";
                    }
                    if ("invicta".equalsIgnoreCase(parameters.get(0))){
                        foreground = "foregroundPorto.png";
                    }
                    if ("sop".equalsIgnoreCase(parameters.get(0))){
                        foreground = "foregroundSop.png";
                    }
                    if ("tiamat".equalsIgnoreCase(parameters.get(0))){
                        foreground = "foregroundTiamat.png";
                    }
                    date=parameters.get(1);
                    firstLine = false;
                    continue;
                }
                parameters = Arrays.asList(line.split(";"));
                parameters.replaceAll(String::trim);
                parameters.replaceAll(String::toLowerCase);
                try {
                    Fighter p1 = new Fighter(parameters.get(0), parameters.get(2), parameters.get(2), Integer.parseInt(parameters.get(4)), false);
                    Fighter p2 = new Fighter(parameters.get(1), parameters.get(3), parameters.get(3), Integer.parseInt(parameters.get(5)), false);
                    p1.setFlip(readFlipFile(p1));
                    p2.setFlip(!readFlipFile(p2));
                    generateThumbnail(foreground, saveLocally, parameters.get(6), date, p1, p2);
                }catch(OnlineImageNotFoundException e) {
                    invalidLines.add(e.getMessage() + " -> " + line);
                }catch(LocalImageNotFoundException e) {
                    alertFactory.displayError(e.getMessage());
                    throw new ThumbnailFromFileException();
                }catch(FontNotFoundException e){
                    throw e;
                }catch (Exception e){
                    invalidLines.add("Invalid line -> "+ line);
                }
            }
        }catch (FileNotFoundException e){
            alertFactory.displayError("Could not file: " + file.getPath());
        }catch (IOException e){
            alertFactory.displayError("Could not correctly parse line: "+line);
        }

        if (!invalidLines.isEmpty()){
            String details = "";
            for (String  l :invalidLines){
                details += l + System.lineSeparator() + System.lineSeparator();
            }
            alertFactory.displayError("Thumbnails could not be generated from these lines: ", details);
            throw new ThumbnailFromFileException();
        }
    }

    private boolean readFlipFile(Fighter fighter){
        String urlNameOriginal = fighter.getUrlName();
        FighterImage.MultiFighters(fighter);
        boolean result = false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("assets/config/flip.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(fighter.getUrlName())){
                    String[] words = line.split(" ");
                    if (words.length>1)
                        result = Boolean.parseBoolean(words[1]);
                    break;
                }
            }
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }

        fighter.setUrlName(urlNameOriginal);
        return result;
    }


}
