package ui.filechooser;

import javafx.stage.FileChooser;

import java.io.File;

public class FileChooserFactory {

    public static FileChooser createDefaultFileChooser(){
        File workingDirectory = new File(System.getProperty("user.dir"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(workingDirectory);
        return fileChooser;
    }
}
