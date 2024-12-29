package app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tournament.TournamentUtils;
import ui.controller.ThumbnailGeneratorController;

public class App extends Application {

    private final Logger LOGGER = LogManager.getLogger(App.class);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Starting application.");
        this.startApp(primaryStage);
    }

    public static void startApp(Stage primaryStage){
        TournamentUtils.initTournamentsListAndSettings();
        try {
            FXMLLoader loader = new FXMLLoader(ThumbnailGeneratorController.class.getClassLoader().getResource("ui/fxml/thumbnailGenerator.fxml"));
            Parent root = (Parent) loader.load();
            root.setId("mainWindow");
            ((ThumbnailGeneratorController) loader.getController()).setStage(primaryStage);
            primaryStage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
            primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");
            primaryStage.setScene(new Scene(root, 740, 680));
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
