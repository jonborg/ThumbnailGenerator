package ui.menu;

import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainMenu {

    private TabPane tabPane;

    private ThumbnailMenu thumbnailMenu;
    private Top8Menu top8Menu;

    public MainMenu(Stage primaryStage){
        top8Menu = new Top8Menu(primaryStage);
        thumbnailMenu = new ThumbnailMenu(primaryStage);

        tabPane = new TabPane();
        tabPane.getTabs().add(thumbnailMenu.getTab());
        tabPane.getTabs().add(top8Menu.getTab());
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}
