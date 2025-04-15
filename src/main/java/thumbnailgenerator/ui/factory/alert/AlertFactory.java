package thumbnailgenerator.ui.factory.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class AlertFactory {
    static private Alert alert;
    static private TextArea textArea;

    public AlertFactory(){
        super();
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);
    }

    public static void displayInfo(String contextText){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Info");
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    public static void displayWarning(String contextText){
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning");
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    public static boolean displayConfirmation(String contextText){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText(contextText);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            return true;
        }else{
            return false;
        }
    }

    public static void displayError(String ... contextText){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(contextText[0]);
        if (contextText.length>1 && !contextText[1].isEmpty())
            writeAdditionalText(contextText[1]);
        alert.showAndWait();
    }

    private static void writeAdditionalText(String additionalText){

        textArea = new TextArea(additionalText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane gridPane = new GridPane();
        gridPane.add(textArea, 0, 0);
        alert.getDialogPane().setExpanded(true);
        alert.getDialogPane().setExpandableContent(gridPane);
    }

}
