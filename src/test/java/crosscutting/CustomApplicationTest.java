package crosscutting;

import enums.CheckBoxId;
import enums.ComboBoxId;
import enums.SpinnerId;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.framework.junit5.ApplicationTest;
import enums.ButtonId;
import enums.TextFieldId;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import static org.testfx.api.FxToolkit.registerPrimaryStage;

public class CustomApplicationTest extends ApplicationTest {
    //@BeforeAll
    public static void setupSpec() throws Exception {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
        registerPrimaryStage();
    }

    public void clickOnButton(ButtonId buttonId){
        clickOn(buttonId.getValue());
    }

    public void writeInTextField(TextFieldId textFieldId, String text){
        clickOn(textFieldId.getValue()).write(text);
    }

    public <T> void selectInComboBox(ComboBoxId textFieldId, String selection){
        clickOn(textFieldId.getValue()).clickOn(selection);
    }

    public void clickOnButton(String parentFxml, ButtonId buttonId){
        Button button = findElement(parentFxml, buttonId.getValue());
        clickOn(button);
    }

    public void writeInTextField(String parentFxml, TextFieldId textFieldId, String text){
        TextField textField = findElement(parentFxml, textFieldId.getValue());
        clickOn(textField).write(text);
    }

    public <T> void selectInComboBox(String parentFxml, ComboBoxId comboBoxId, String selection){
        ComboBox<T> comboBox = findElement(parentFxml, comboBoxId.getValue());
        clickOn(comboBox).clickOn(selection);
    }

    public void setCheckBox(CheckBoxId checkBoxId, boolean value){
        if (value) {
            clickOn(checkBoxId.getValue());
        }
    }

    public <T> void writeAndSelectInComboBox(String parentFxml, ComboBoxId comboBoxId, String selection){
        ComboBox<T> comboBox = findElement(parentFxml, comboBoxId.getValue());
        clickOn(comboBox).write(selection).clickOn(selection);
    }

    public <T> void writeInSpinner(String parentFxml, SpinnerId spinnerId, String value){
        Spinner<T> spinner = findElement(parentFxml, spinnerId.getValue());
        doubleClickOn(spinner)
                .write(value)
                .type(KeyCode.ENTER);
    }

    public void setCheckBox(String parentFxml, CheckBoxId checkBoxId, boolean value){
        if (value) {
            CheckBox checkBox = findElement(parentFxml, checkBoxId.getValue());
            clickOn(checkBox);
        }

    }

    private <T> T findElement(String parent, String id){
        return (T) lookup(parent).lookup(id).query();
    }
}
