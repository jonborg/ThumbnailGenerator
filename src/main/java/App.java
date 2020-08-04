import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.menu.MainMenu;

public class App extends Application {

    private MainMenu mainMenu;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.getIcons().add(new Image("https://pbs.twimg.com/profile_images/1132749237945552901/v74uelMr_400x400.png"));
        primaryStage.setTitle("Smash Bros. VOD Thumbnail Generator");

        mainMenu = new MainMenu(primaryStage);

        StackPane root = new StackPane();
        root.getChildren().add(mainMenu.getTabPane());
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
    }
}
