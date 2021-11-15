package ui.textfield;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChosenImageField extends StackPane {
    protected TextField textField;
    protected Button button;
    protected String description;
    protected List<String> extensions;

    public ChosenImageField(){
        super();
        description = "Image Files";
        extensions = new ArrayList<>();
        extensions.add("*.jpg");
        extensions.add("*.png");

        initComponents();
    }

    protected void initComponents(){
        initTextField();
        initButton();

        HBox hbox = new HBox(textField, button);
        this.getChildren().addAll(hbox);
    }

    protected void initTextField(){
        textField = new TextField();
        textField.setMinWidth(180);
        textField.setPrefWidth(180);
        textField.setMaxWidth(180);
    }

    protected void initButton(){
        button = new Button();

        button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/ui/images/folder.png"))));
        button.setMinWidth(30);
        button.setPrefWidth(30);
        button.setMaxWidth(30);

        button.setMinHeight(30);
        button.setPrefHeight(30);
        button.setMaxHeight(30);
        button.setOnAction(action -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(description, extensions));

            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                this.setText(file.getPath());
            }
        });
    }

    public void setText(String text){
        this.textField.setText(text);
    }

    public String getText(){
        return this.textField.getText();
    }
}
