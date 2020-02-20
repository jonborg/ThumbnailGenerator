import javafx.scene.control.Alert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ThumbnailFromFile extends Thumbnail {

    public void generateFromFile(File file, boolean saveLocally){
        boolean firstLine = true;
        String foreground = null;
        String date = null;
        String line = null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
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
                    date=parameters.get(1);
                    firstLine = false;
                    continue;
                }
                parameters = Arrays.asList(line.split(";"));
                parameters.replaceAll(String::trim);
                parameters.replaceAll(String::toLowerCase);
                Fighter p1 = new Fighter(parameters.get(0),parameters.get(2), Integer.parseInt(parameters.get(4)), false);
                Fighter p2 = new Fighter(parameters.get(1), parameters.get(3), Integer.parseInt(parameters.get(5)), false);
                p1.setFlip(readFlipFile(p1));
                p2.setFlip(!readFlipFile(p2));
                generateThumbnail(foreground, saveLocally, parameters.get(6),date, p1, p2);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR");
            alert.setContentText("Could not correctly parse line: "+line);
            alert.showAndWait();

        }
    }

    private boolean readFlipFile(Fighter fighter){
        String urlNameOriginal = fighter.getUrlName();
        FighterImage.MultiFighters(fighter);
        boolean result = false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("resources/config/flip.txt"), StandardCharsets.UTF_8))) {
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
