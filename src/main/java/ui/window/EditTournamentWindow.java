package ui.window;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import ui.tournament.Tournament;

import java.util.List;


public class EditTournamentWindow extends Window {
    private int WIDTH = 500;
    private int HEIGHT = 420;

    private ListView<Tournament> tournamentList;

    private Button addButton = createButton("Add");
    private Button editButton = createButton("Edit");
    private Button deleteButton = createButton("Delete");

    private Button saveButton = createButton("Save");
    private Button cancelButton = createButton("Cancel");

    public EditTournamentWindow(List<Tournament> tournaments){
        super();
        Stage newWindow = new Stage();
        newWindow.setTitle("Edit Tournaments");

        Label listLabel = new Label("Tournaments' list");

        tournamentList = initListView(tournaments);

        setListeners();

        VBox listContent = new VBox(listLabel, tournamentList);
        listContent.setSpacing(10);
        listContent.setAlignment(Pos.CENTER_LEFT);

        VBox editButtons = new VBox(addButton, editButton, deleteButton);
        editButtons.setSpacing(10);

        VBox saveButtons = new VBox(saveButton, cancelButton);
        saveButtons.setSpacing(10);

        VBox buttons = new VBox(editButtons, saveButtons);
        buttons.setSpacing(80);
        buttons.setAlignment(Pos.CENTER);

        HBox allContent = new HBox(listContent, buttons);
        allContent.setSpacing(30);
        allContent.setAlignment(Pos.CENTER);


        StackPane root = new StackPane();
        root.getChildren().add(allContent);
        newWindow.setScene(new Scene(root, WIDTH, HEIGHT));
        newWindow.show();
    }

    private ListView<Tournament> initListView(List<Tournament> tournaments) {
        ObservableList<Tournament> observableList = FXCollections.observableArrayList();
        observableList.addAll(tournaments);

        ListView<Tournament> tournamentList = new ListView(observableList);

        tournamentList.setPrefSize(300,300);
        tournamentList.setMaxSize(300, 300);

        tournamentList.setCellFactory(param -> new ListCell<Tournament>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        return tournamentList;
    }


    private void setListeners(){
        deleteButton.setOnAction(event -> {
            System.out.println(tournamentList.getSelectionModel().getSelectedIndex());
            tournamentList.getItems().remove(
                tournamentList.getSelectionModel().getSelectedIndex()
            );
        });

        saveButton.setOnAction(event -> {
         //   ThumbnailMainWindow.updateTournamentList(tournamentList.getItems());
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        });

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    private Button createButton(String text){
        Button button = new Button(text);
        button.setPrefSize(80,20);
        return button;
    }
}
