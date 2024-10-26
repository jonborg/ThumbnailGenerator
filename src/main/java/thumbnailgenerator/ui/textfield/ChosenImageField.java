package thumbnailgenerator.ui.textfield;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import lombok.var;
import ui.filechooser.FileChooserFactory;

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
        var buttonSideLength = 25;
        button = new Button();

        button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/thumbnailgenerator/ui/images/folder.png"))));
        button.setMinWidth(buttonSideLength);
        button.setPrefWidth(buttonSideLength);
        button.setMaxWidth(buttonSideLength);

        button.setMinHeight(buttonSideLength);
        button.setPrefHeight(buttonSideLength);
        button.setMaxHeight(buttonSideLength);
        button.setOnAction(action -> {
            var fileChooser = FileChooserFactory.createDefaultFileChooser();
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
