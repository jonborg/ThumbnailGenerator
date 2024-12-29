package utils;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class FileUtils {

    private FileUtils(){
        throw new UnsupportedOperationException("Utils class");
    }

    public static File getFileFromResources(String path){
        return new File(FileUtils.class.getResource(path).getPath());
    }

    public static File getActualFile(String path) {
        return new File(System.getProperty("user.dir") + path);
    }

    public static File getCharacterImage(String characterUrlName, int alt){
        return FileUtils.getActualFile("/assets/fighters/" + characterUrlName + "/" + alt + ".png");
    }

    public static File getMostRecentFile(String folderPath){
        return Arrays.stream(
                Objects.requireNonNull(getActualFile(folderPath).listFiles()))
                .filter(f -> !f.isDirectory())
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);
    }
}
