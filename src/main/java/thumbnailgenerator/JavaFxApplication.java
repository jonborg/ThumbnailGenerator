package thumbnailgenerator;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import thumbnailgenerator.service.TournamentUtils;
import thumbnailgenerator.ui.controller.ThumbnailGeneratorController;

public class JavaFxApplication extends Application {

    private final Logger LOGGER = LogManager.getLogger(JavaFxApplication.class);
    private ConfigurableApplicationContext applicationContext;
    private static Parent root;

    @Override
    public void init() throws IOException {
        TournamentUtils.initTournamentsListAndSettings();
        applicationContext = new SpringApplicationBuilder(Main.class).run();
        FXMLLoader fxmlLoader = new FXMLLoader(ThumbnailGeneratorController.class.getClassLoader().getResource(
                "thumbnailgenerator/ui/fxml/thumbnailGenerator.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        root = fxmlLoader.load();

    }

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Starting application.");
        startApp(primaryStage);
    }

    public static void startApp(Stage primaryStage){
        primaryStage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
        primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");
        primaryStage.setScene(new Scene(root, 740, 680));
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}
