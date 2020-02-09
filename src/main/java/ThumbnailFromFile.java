import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThumbnailFromFile extends Thumbnail {

    public void generateFromFile(File file, String foreground, boolean saveLocally){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> parameters = Arrays.asList(line.split(";"));
                parameters.replaceAll(String::trim);
                parameters.replaceAll(String::toLowerCase);
                Fighter p1 = new Fighter(parameters.get(0),parameters.get(2), Integer.parseInt(parameters.get(4)), false);
                Fighter p2 = new Fighter(parameters.get(1), parameters.get(3), Integer.parseInt(parameters.get(5)), false);
                generateThumbnail(foreground, saveLocally, "","", p1, p2);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
