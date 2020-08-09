package ui.placement;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import fighter.Fighter;
import placement.Placement;
import ui.factory.alert.AlertFactory;

import ui.player.Names;

public class PlacementPane extends VBox {
    AlertFactory alertFactory = new AlertFactory();

    private Map<String,String> map = Names.map;

    private TextField player = new TextField();
    private ComboBox<String> fighter1 = new ComboBox<>();
    private Spinner<Integer> alt1 = new Spinner<>();
    private ComboBox<String> fighter2 = new ComboBox<>();
    private Spinner<Integer> alt2 = new Spinner<>();
    private ComboBox<String> fighter3 = new ComboBox<>();
    private Spinner<Integer> alt3 = new Spinner<>();

    private int playerNumber;

    String urlName1;
    String urlName2;
    String urlName3;

    private PlacementPane(){
        super();
    }

    public PlacementPane(int playerNumber){
        this();
        this.playerNumber = playerNumber;

        player.setMaxWidth(180);

        initFighterSelection();
        alt1.setMaxWidth(80);
        alt1.setEditable(true);
        alt1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));

        alt2.setMaxWidth(80);
        alt2.setEditable(true);
        alt2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));

        alt3.setMaxWidth(80);
        alt3.setEditable(true);
        alt3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));

        VBox playerBox = new VBox(new Label("Player "+ playerNumber + ":"), player);
        VBox char1Box = new VBox (new Label("Main character:"), fighter1);
        VBox alt1Box = new VBox(new Label("Alt nº:"), alt1);
        VBox char2Box = new VBox (new Label("2nd Character:"), fighter2);
        VBox alt2Box = new VBox(new Label("Alt nº:"), alt2);
        VBox char3Box = new VBox (new Label("3rd Character:"), fighter3);
        VBox alt3Box = new VBox(new Label("Alt nº:"), alt3);
        HBox allPlacement1Box = new HBox(playerBox,char1Box,alt1Box,char2Box,alt2Box,char3Box,alt3Box);
        allPlacement1Box.setSpacing(5);

        this.getChildren().addAll(allPlacement1Box);
        this.setSpacing(10);

    }

    private String getSelectionNameF1() {
        String sel = fighter1.getSelectionModel().getSelectedItem();
        if (sel == null || sel.equals("Mii Brawler") || sel.equals("Mii Swordfighter")) {
            alt1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1));
        } else {
            if (sel.equals("Mii Gunner")) {
                alt1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
            } else {
                alt1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));
            }
        }
        return map.get(sel);
    }

    private String getSelectionNameF2() {
        String sel = fighter2.getSelectionModel().getSelectedItem();
        if (sel == null || sel.equals("Mii Brawler") || sel.equals("Mii Swordfighter")) {
            alt2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1));
        } else {
            if (sel.equals("Mii Gunner")) {
                alt2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
            } else {
                alt2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));
            }
        }
        return map.get(sel);
    }

    private String getSelectionNameF3() {
        String sel = fighter3.getSelectionModel().getSelectedItem();
        if (sel == null || sel.equals("Mii Brawler") || sel.equals("Mii Swordfighter")) {
            alt3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1));
        } else {
            if (sel.equals("Mii Gunner")) {
                alt3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
            } else {
                alt3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));
            }
        }
        return map.get(sel);
    }

    private void setDefaultFlip (String urlName){
        boolean skip = false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("assets/config/fighters/flip.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (urlName == "falco" && !skip && line.contains(urlName)){
                    skip = true;
                    continue;
                }
                if (line.contains(urlName)){
                    break;
                }
            }
        }catch(IOException e){
            alertFactory.displayError("Could not detect flip.txt");
        }

    }

    private void initFighterSelection(){
        fighter1.setEditable(true);
        fighter1.setMaxWidth(180);
        fighter2.setEditable(true);
        fighter2.setMaxWidth(180);
        fighter3.setEditable(true);
        fighter3.setMaxWidth(180);
        ObservableList<String> items = FXCollections.observableArrayList(map.keySet());
        FilteredList<String> filteredItems = new FilteredList<>(items);

        fighter1.getEditor().textProperty().addListener(new InputFilter(fighter1, filteredItems, false));
        fighter1.setItems(filteredItems);

        fighter2.getEditor().textProperty().addListener(new InputFilter(fighter2, filteredItems, false));
        fighter2.setItems(filteredItems);

        fighter3.getEditor().textProperty().addListener(new InputFilter(fighter3, filteredItems, false));
        fighter3.setItems(filteredItems);

        fighter1.setOnAction(actionEvent -> {
            urlName1 = getSelectionNameF1();
            setDefaultFlip(urlName1);
        });

        fighter2.setOnAction(actionEvent -> {
            urlName2 = getSelectionNameF2();
            setDefaultFlip(urlName2);
        });

        fighter3.setOnAction(actionEvent -> {
            urlName3 = getSelectionNameF3();
            setDefaultFlip(urlName3);
        });

    }

    public Placement generatePlacement(){
        ArrayList<Fighter> fighters = new ArrayList<>();
        fighters.add(new Fighter(getPlayer(), getFighter1(), getUrlName1(), getAlt1(), false));
        fighters.add(new Fighter(getPlayer(), getFighter2(), getUrlName2(), getAlt2(), false));
        fighters.add(new Fighter(getPlayer(), getFighter3(), getUrlName3(), getAlt3(), false));
        return new Placement(playerNumber, getPlayer(), fighters);
    }

    public String getPlayer(){
        return this.player.getText().toUpperCase();
    }

    public void setPlayer(String player){
        this.player.setText(player);
    }

    public String getUrlName1(){
        return this.urlName1;
    }

    public void setUrlName1(){
        this.urlName1 = urlName1;
    }

    public String getUrlName2(){
        return this.urlName2;
    }

    public void setUrlName2(){
        this.urlName2 = urlName2;
    }

    public String getUrlName3(){
        return this.urlName3;
    }

    public void setUrlName3(){
        this.urlName3 = urlName3;
    }

    public String getFighter1(){
        return fighter1.getSelectionModel().getSelectedItem();
    }

    public void setFighter1(String fighter1){
        this.fighter1.getSelectionModel().select(fighter1);
    }

    public String getFighter2(){
        return fighter2.getSelectionModel().getSelectedItem();
    }

    public void setFighter2(String fighter2){
        this.fighter2.getSelectionModel().select(fighter2);
    }

    public String getFighter3(){
        return fighter3.getSelectionModel().getSelectedItem();
    }

    public void setFighter3(String fighter3){
        this.fighter3.getSelectionModel().select(fighter3);
    }

    public int getAlt1(){
        return this.alt1.getValue();
    }

    public void setAlt1(int alt1){
        this.alt1.getValueFactory().setValue(alt1);
    }

    public int getAlt2(){
        return this.alt2.getValue();
    }

    public void setAlt2(int alt2){
        this.alt2.getValueFactory().setValue(alt2);
    }

    public int getAlt3(){
        return this.alt3.getValue();
    }

    public void setAlt3(int alt3){
        this.alt3.getValueFactory().setValue(alt3);
    }




    private class InputFilter implements ChangeListener<String> {

        private ComboBox<String> box;
        private FilteredList<String> items;
        private boolean upperCase;
        private int maxLength;
        private String restriction;

        /**
         * @param box
         *            The combo box to whose textProperty this listener is
         *            added.
         * @param items
         *            The {@link FilteredList} containing the items in the list.
         */
        public InputFilter(ComboBox<String> box, FilteredList<String> items, boolean upperCase, int maxLength,
                           String restriction) {
            this.box = box;
            this.items = items;
            this.upperCase = upperCase;
            this.maxLength = maxLength;
            this.restriction = restriction;
        }

        public InputFilter(ComboBox<String> box, FilteredList<String> items, boolean upperCase, int maxLength) {
            this(box, items, upperCase, maxLength, null);
        }

        public InputFilter(ComboBox<String> box, FilteredList<String> items, boolean upperCase) {
            this(box, items, upperCase, -1, null);
        }

        public InputFilter(ComboBox<String> box, FilteredList<String> items) {
            this(box, items, false);
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            StringProperty value = new SimpleStringProperty(newValue);

            // If any item is selected we save that reference.
            String selected = box.getSelectionModel().getSelectedItem() != null
                    ? box.getSelectionModel().getSelectedItem() : null;

            String selectedString = null;
            // We save the String of the selected item.
            if (selected != null) {
                selectedString = (String) selected;
            }

            if (upperCase) {
                value.set(value.get().toUpperCase());
            }

            if (maxLength >= 0 && value.get().length() > maxLength) {
                value.set(oldValue);
            }

            if (restriction != null) {
                if (!value.get().matches(restriction + "*")) {
                    value.set(oldValue);
                }
            }

            // If an item is selected and the value in the editor is the same
            // as the selected item we don't filter the list.
            if (selected != null && value.get().equals(selectedString)) {
                // This will place the caret at the end of the string when
                // something is selected.
                Platform.runLater(() -> box.getEditor().end());
            } else {
                items.setPredicate(item -> {
                    String itemString = item;
                    if (itemString.toUpperCase().contains(value.get().toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }

            // If the popup isn't already showing we show it.
            if (!box.isShowing()) {
                // If the new value is empty we don't want to show the popup,
                // since
                // this will happen when the combo box gets manually reset.
                if (!newValue.isEmpty() && box.isFocused()) {
                    box.show();
                }
            }
            // If it is showing and there's only one item in the popup, which is
            // an
            // exact match to the text, we hide the dropdown.
            else {
                if (items.size() == 1) {
                    // We need to get the String differently depending on the
                    // nature
                    // of the object.
                    String item = items.get(0);

                    // To get the value we want to compare with the written
                    // value, we need to crop the value according to the current
                    // selectionCrop.
                    String comparableItem = item;

                    if (value.get().equals(comparableItem)) {
                        Platform.runLater(() -> box.hide());
                    }
                }
            }

            box.getEditor().setText(value.get());
        }
    }
}
