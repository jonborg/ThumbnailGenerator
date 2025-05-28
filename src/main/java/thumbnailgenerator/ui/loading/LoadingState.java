package thumbnailgenerator.ui.loading;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import thumbnailgenerator.enums.LoadingType;

public class LoadingState {
    private BooleanProperty isLoading;
    private DoubleProperty progress;
    private StringProperty loadingText;

    public LoadingState(boolean isLoading, LoadingType loadingType, int currentQuantity, int totalQuantity) {
        this.isLoading = new SimpleBooleanProperty(isLoading);
        this.progress = new SimpleDoubleProperty((double) currentQuantity/totalQuantity);
        generateText(loadingType, currentQuantity, totalQuantity);
    }

    public void update(boolean isLoading, LoadingType loadingType, int currentQuantity, int totalQuantity) {
        Platform.runLater(() -> {
            this.isLoading.setValue(isLoading);
            generateText(loadingType, currentQuantity, totalQuantity);
        });
    }

    public void disableLoading() {
        update(false, LoadingType.THUMBNAIL, 0, 0);
    }

    private void generateText(LoadingType loadingType, int currentQuantity, int totalQuantity) {
        var string = "";
        switch (loadingType){
            case THUMBNAIL:
                string = "Generating thumbnail: " + currentQuantity + "/" + totalQuantity;
                break;
            case TOP8:
                string = "Generating top8: " + currentQuantity + "/" + totalQuantity;
                break;
            case THUMBNAIL_POSITION_CONVERSION:
                string = "Converting character positions: " + currentQuantity + "/" + totalQuantity;
        }
        if (this.loadingText == null) {
            this.loadingText = new SimpleStringProperty(string);
        } else {
            this.loadingText.setValue(string);
        }
    }

    public BooleanProperty isLoadingProperty() {
        return isLoading;
    }

    public StringProperty getLoadingText(){
        return loadingText;
    }

    public DoubleProperty getProgress(){
        return progress;
    }
}
