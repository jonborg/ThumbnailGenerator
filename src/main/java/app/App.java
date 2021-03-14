package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tournament.TournamentUtils;
import ui.controller.ThumbnailGeneratorController;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.startApp(primaryStage);
    }

    public static void startApp(Stage primaryStage){
        TournamentUtils.initTournamentsListAndSettings();
        try {
            FXMLLoader loader = new FXMLLoader(ThumbnailGeneratorController.class.getClassLoader().getResource("ui/fxml/thumbnailGenerator.fxml"));
            Parent root = (Parent) loader.load();
            ((ThumbnailGeneratorController) loader.getController()).setStage(primaryStage);

            //primaryStage.getIcons().add(new Image(ThumbnailGeneratorController.class.getResourceAsStream("/logo/smash_ball.png")));
            primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");
            primaryStage.setScene(new Scene(root, 800, 660));
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
