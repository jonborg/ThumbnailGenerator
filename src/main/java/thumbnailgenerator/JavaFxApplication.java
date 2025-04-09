package thumbnailgenerator;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import thumbnailgenerator.config.ApplicationContextProvider;
import thumbnailgenerator.config.SpringFXMLLoader;
import thumbnailgenerator.service.TournamentUtils;
import thumbnailgenerator.ui.controller.ThumbnailGeneratorController;
import thumbnailgenerator.ui.factory.alert.AlertFactory;

public class JavaFxApplication extends Application {

    private final Logger LOGGER = LogManager.getLogger(JavaFxApplication.class);
    private ConfigurableApplicationContext applicationContext;
    private static Parent root;

    @Override
    public void init() throws IOException {
        TournamentUtils.initTournamentsListAndSettings();
        applicationContext = new SpringApplicationBuilder(Main.class).run();
        ApplicationContextProvider.setApplicationContext(applicationContext);
        SpringFXMLLoader fxmlLoader = new SpringFXMLLoader("thumbnailgenerator/ui/fxml/thumbnailGenerator.fxml");
        root = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Starting application.");
        Thread.setDefaultUncaughtExceptionHandler(JavaFxApplication::handleGlobalException);
        startApp(primaryStage);
    }

    public static void startApp(Stage primaryStage){
        primaryStage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
        primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");
        primaryStage.setScene(new Scene(root, 740, 760));
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    private static void handleGlobalException(Thread t, Throwable e) {
        System.err.println("An unexpected error occurred in thread " + t.getName() + ": " + e.getMessage());
        e.printStackTrace();
        Platform.runLater(() -> {
            AlertFactory.displayError("An unexpected error occurred:\n",
                    ExceptionUtils.getStackTrace(e));
        });
    }

}
