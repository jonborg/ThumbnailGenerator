package crosscutting;

import enums.CheckBoxId;
import enums.ChosenImageFieldId;
import enums.ChosenJsonFieldId;
import enums.ComboBoxId;
import enums.MenuId;
import enums.ScrollPaneId;
import enums.SpinnerId;
import enums.WindowId;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.testfx.framework.junit5.ApplicationTest;
import enums.ButtonId;
import enums.TextFieldId;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import thumbnailgenerator.JavaFxApplication;
import thumbnailgenerator.ui.textfield.ChosenImageField;
import thumbnailgenerator.ui.textfield.ChosenJsonField;
import utils.WaitUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomApplicationTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws IOException {
        JavaFxApplication javaFxApplication = new JavaFxApplication();
        javaFxApplication.init();
        javaFxApplication.start(stage);
    }

    public void clickOnButton(ButtonId buttonId){
        moveTo(buttonId.getValue()).clickOn(buttonId.getValue());
    }

    public void writeInTextField(TextFieldId textFieldId, String text){
        TextField textField = findElement(textFieldId.getValue());
        textField.clear();
        clickOn(textField).write(text);
    }

    public void writeInTextField(TextFieldId textFieldId, int value){
        writeInTextField(textFieldId, String.valueOf(value));
    }

    public void writeInTextField(TextFieldId textFieldId, float value){
        writeInTextField(textFieldId, String.valueOf(value));
    }

    public <T> void selectInComboBox(ComboBoxId comboBoxId, String selection){
        clickOn(comboBoxId.getValue()).clickOn(selection);
    }

    public <T> void writeInComboBox(ComboBoxId comboBoxId, String value){
        clickOn(comboBoxId.getValue()).write(value).clickOn(value);
    }

    public void clickOnButton(String parentFxml, ButtonId buttonId){
        Button button = findElement(buttonId.getValue(), parentFxml);
        clickOn(button);
    }

    public void clickOnButton(Scene scene, ButtonId buttonId){
        Button button = findElement(buttonId.getValue(), scene);
        clickOn(button);
    }

    public void writeInTextField(String parentFxml, TextFieldId textFieldId, String text){
        TextField textField = findElement(textFieldId.getValue(), parentFxml);
        textField.clear();
        clickOn(textField).write(text);
    }

    public void writeInTextField(Scene scene, TextFieldId textFieldId, String text){
        TextField textField = findElement(textFieldId.getValue(), scene);
        textField.clear();
        clickOn(textField).write(text);
    }

    public <T> void selectInComboBox(String parentFxml, ComboBoxId comboBoxId, String selection){
        ComboBox<T> comboBox = findElement(comboBoxId.getValue(), parentFxml);
        clickOn(comboBox).clickOn(selection);
    }

    public void setCheckBox(CheckBoxId checkBoxId, boolean value){
        if (value) {
            clickOn(checkBoxId.getValue());
        }
    }

    public void clickOnMenuOption(MenuId menuId){
        clickOn(menuId.getValue());
    }

    public void clickOnMenuOptionThenMove(MenuId menuId, MenuId moveTo){
        clickOn(menuId.getValue()).moveTo(moveTo.getValue());
    }

    public <T> void writeAndSelectInComboBox(String parentFxml, ComboBoxId comboBoxId, String selection){
        ComboBox<T> comboBox = findElement(comboBoxId.getValue(), parentFxml);
        clickOn(comboBox).write(selection).clickOn(selection);
    }

    public <T> void writeInSpinner(String parentFxml, SpinnerId spinnerId, String value){
        Spinner<T> spinner = findElement(spinnerId.getValue(), parentFxml);
        doubleClickOn(spinner)
                .write(value)
                .type(KeyCode.ENTER);
    }

    public void writeInChosenImageField(ChosenImageFieldId chosenImageFieldId, String value){
        ChosenImageField chosenImageField = findElement(chosenImageFieldId.getValue());
        clickOn(chosenImageField.getTextField()).write(value);
    }

    public void writeInChosenJsonField(ChosenJsonFieldId chosenImageFieldId, String value){
        ChosenJsonField chosenJsonField = findElement(chosenImageFieldId.getValue());
        clickOn(chosenJsonField.getTextField()).write(value);
    }

    public void setCheckBox(String parentFxml, CheckBoxId checkBoxId, boolean value){
        if (value) {
            CheckBox checkBox = findElement(checkBoxId.getValue(), parentFxml);
            clickOn(checkBox);
        }
    }

    public Window getWindow(WindowId windowId){
        return listWindows()
                .stream()
                .filter(window ->
                        window.isShowing()
                                && windowId.getValue().equals(
                                        window.getScene().getRoot().getId()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Could not find window with id " + windowId.getValue())
                );
    }

    public void assertEqualsTextFieldContent(TextFieldId textFieldId, Scene scene, String expectedText){
        TextField textField = findElement(textFieldId.getValue(), scene);
        assertEquals(expectedText, textField.getText());
    }

    public void assertEqualsComboBoxSelection(ComboBoxId textFieldId, Scene scene, String expectedText){
        ComboBox comboBox = findElement(textFieldId.getValue(), scene);
        assertEquals(expectedText, comboBox.getSelectionModel().getSelectedItem().toString());
    }

    public void assertEqualsChosenImageFieldContent(ChosenImageFieldId chosenImageFieldId, Scene scene, String expectedText){
        ChosenImageField chosenImageField = findElement(chosenImageFieldId.getValue(), scene);
        assertEquals(expectedText, chosenImageField.getText());
    }

    public void assertEqualsChosenJsonFieldContent(ChosenJsonFieldId chosenJsonFieldId, Scene scene, String expectedText){
        ChosenJsonField chosenJsonField = findElement(chosenJsonFieldId.getValue(), scene);
        assertEquals(expectedText, chosenJsonField.getText());
    }

    public void scrollPaneVertically(ScrollPaneId scrollPaneId, Scene scene, double value){
        ScrollPane scrollPane = findElement(scrollPaneId.getValue(), scene);
        scrollPane.setVvalue(value);
    }

    private <T> T findElement(String id, Scene scene){
        return (T) scene.getRoot().lookup(id);
    }

    private <T> T findElement(String id, String parent){
        return (T) lookup(parent).lookup(id).query();
    }

    private <T> T findElement(String id){
        return (T) lookup(id).query();
    }
}
