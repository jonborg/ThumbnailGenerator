import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ui/fxml/thumbnailGenerator.fxml"));
        primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("/logo/smash_ball.png")));
        primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");
        primaryStage.setScene(new Scene(root, 800, 660));
        primaryStage.show();
    }

}
