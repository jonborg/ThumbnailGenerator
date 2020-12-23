package ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tournament.Tournament;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TournamentsSettingsController implements Initializable {
    @FXML
    private ListView tournamentsListView;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;



    private static List<Tournament> tournamentsList = ThumbnailGeneratorController.getTournamentsList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Tournament> observableList = FXCollections.observableArrayList();
        observableList.addAll(tournamentsList);
        tournamentsListView.setItems(observableList);
        tournamentsListView.setCellFactory(param -> new ListCell<Tournament>() {
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
    }


    public void edit(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/fxml/tournamentsAddEdit.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Tournament");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent actionEvent) {
        if (tournamentsListView.getSelectionModel().getSelectedItem() != null){
            int i = tournamentsListView.getSelectionModel().getSelectedIndex();
            tournamentsListView.getItems().remove(i);
            tournamentsList.remove(i);
        }
    }

    public void save (ActionEvent actionEvent){
        cancel(actionEvent);
        ThumbnailGeneratorController.updateTournamentsList(tournamentsList);
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}
