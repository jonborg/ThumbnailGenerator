package ui.controller;

import fighter.DownloadFighterURL;
import fighter.Fighter;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ui.factory.alert.AlertFactory;
import fighter.Names;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.ResourceBundle;

public class Player1Controller implements Initializable {
    @FXML
    protected TextField player;
    @FXML
    protected ComboBox<String> fighter;
    @FXML
    protected CheckBox flip;
    @FXML
    protected Spinner<Integer> alt;
    @FXML
    protected Hyperlink iconLink;
    @FXML
    protected ImageView icon;
    @FXML
    protected Hyperlink icon2Link;
    @FXML
    protected ImageView icon2;
    @FXML
    protected HBox colorBox;


    protected Map<String,String> map = Names.map;
    protected String flipFile = "settings/thumbnails/images/flip.txt";
    protected String urlName;

    protected AlertFactory alertFactory = new AlertFactory();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFighters();
        initAlts();
    }

    protected void initFighters(){
        ObservableList<String> items = FXCollections.observableArrayList(map.keySet());
        FilteredList<String> filteredItems = new FilteredList<>(items);

        fighter.getEditor().textProperty().addListener(new InputFilter(fighter, filteredItems, false));
        fighter.setItems(filteredItems);
    }

    protected void initAlts(){
        alt.valueProperty().addListener(((observable, oldValue, newValue) -> updateFighterIcon()));
    }


    public void selectFighter(ActionEvent actionEvent) {
        urlName = getSelectionName();
        setDefaultFlip(urlName);
        updateFighterIcon();
    }

    protected String getSelectionName() {
        String sel = fighter.getSelectionModel().getSelectedItem();
        if (sel == null || sel.equals("Mii Brawler") || sel.equals("Mii Swordfighter")) {
            alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1));
        } else {
            if (sel.equals("Mii Gunner")) {
                alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
            } else {
                alt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8));
            }
        }
        return sel == null ? null : map.get(sel);
    }

    protected void setDefaultFlip (String urlName){
        boolean skip = false;
        if (urlName == null){
            flip.setSelected(false);
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(flipFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (urlName == "falco" && !skip && line.contains(urlName)){
                    skip = true;
                    continue;
                }
                if (line.contains(urlName)){
                    String[] words = line.split(" ");
                    if (words.length>1)
                        flip.setSelected(Boolean.parseBoolean(words[1]));
                    break;
                }
            }
        }catch(IOException e){
            alertFactory.displayError("Could not detect flip.txt");
        }

    }

    protected void updateFighterIcon(){
        try {
            iconLink.setDisable(false);
            icon.setImage(new Image(getClass().getResourceAsStream("/icons/" + urlName + "/" + alt.getValue() + ".png")));
            if ("pyra".equals(urlName)){
                icon2Link.setDisable(false);
                Image imageIcon2 = new Image(getClass().getResourceAsStream("/icons/mythra/" + alt.getValue() + ".png"));
                icon2.setImage(imageIcon2);
                colorBox.setSpacing(0);
            }else{
                icon2Link.setDisable(true);
                icon2.setImage(null);
                colorBox.setSpacing(30);
            }
        }catch (NullPointerException e){
            iconLink.setDisable(true);
            iconLink.setText(null);
            icon.setImage(null);
        }
    }

/*    private void initFighterSelection(){
        fighter.setEditable(true);
        fighter.setMaxWidth(185);
        ObservableList<String> items = FXCollections.observableArrayList(map.keySet());
        FilteredList<String> filteredItems = new FilteredList<>(items);

        fighter.getEditor().textProperty().addListener(new InputFilter(fighter, filteredItems, false));
        fighter.setItems(filteredItems);

        fighter.setOnAction(actionEvent -> {
            urlName = getSelectionName();
            setDefaultFlip(urlName);
            updateFighterIcon();
        });

        alt.valueProperty().addListener(((observable, oldValue, newValue) -> updateFighterIcon()));
        //alt.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> updateFighterIcon()));
    }

    private void updateFighterIcon(){
        try {
            iconLink.setDisable(false);
            iconLink.setOnAction(event -> initIconListener());
            Image imageIcon = new Image(PlayerPane.class.getResourceAsStream("/icons/" + urlName + "/" + alt.getValue() + ".png"));
            icon.setImage(imageIcon);

            if ("pyra".equals(urlName)){
                icon2Link.setDisable(false);
                icon2Link.setOnAction(event -> initIconListener());
                Image imageIcon2 = new Image(PlayerPane.class.getResourceAsStream("/icons/mythra/" + alt.getValue() + ".png"));
                icon2.setImage(imageIcon2);
                colorBox.setSpacing(0);
            }else{
                icon2Link.setDisable(true);
                icon2.setImage(null);
                colorBox.setSpacing(30);
            }
*/

    public void previewFighter(ActionEvent actionEvent) {
        String url = DownloadFighterURL.generateFighterURL(urlName, alt.getValue());
        if(Desktop.isDesktopSupported()) {
            try {
                if ("http".equals(url.substring(0,4))){
                    Desktop.getDesktop().browse(new URI(url));
                } else {
                    Desktop.getDesktop().open(new File(url));
                }
            }catch(URISyntaxException | IOException e ){
                alertFactory.displayError("Could not open the following URL: "+ url, e.getMessage());
            }
        }
    }

    public Fighter generateFighter(){
        return new Fighter(getPlayer(), getFighter() ,getUrlName(), getAlt(), isFlip());
    }

    public String getPlayer(){
        return this.player.getText();
    }

    public void setPlayer(String player){
        this.player.setText(player);
    }

    public String getUrlName(){
        return this.urlName;
    }

    public void setUrlName(){
        this.urlName = urlName;
    }

    public String getFighter(){
        return fighter.getSelectionModel().getSelectedItem();
    }

    public void setFighter(String fighter){
        this.fighter.getSelectionModel().select(fighter);
    }

    public int getAlt(){
        return this.alt.getValue();
    }

    public void setAlt(int alt){
        this.alt.getValueFactory().setValue(alt);
    }

    public boolean isFlip(){
        return this.flip.isSelected();
    }

    public void setFlip(boolean flip){
        this.flip.setSelected(flip);
    }





    protected class InputFilter implements ChangeListener<String> {

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
