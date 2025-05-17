package thumbnailgenerator.ui.loading;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoadingState {
    private BooleanProperty isLoading;
    private DoubleProperty progress;
    private StringProperty loadingText;

    public LoadingState(boolean isLoading, boolean isThumbnail,
                        int quantityGenerated, int totalQuantity) {
        this.isLoading = new SimpleBooleanProperty(isLoading);
        this.progress = new SimpleDoubleProperty((double) quantityGenerated/totalQuantity);
        if (isThumbnail) {
            this.loadingText = new SimpleStringProperty(
                    "Generating thumbnail: " + quantityGenerated + "/" + totalQuantity
            );
        } else {
            this.loadingText = new SimpleStringProperty(
                    "Generating top8: " + quantityGenerated + "/" + totalQuantity
            );
        }
    }

    public void update(boolean isLoading, boolean isThumbnail, int quantityGenerated, int totalQuantity) {
        Platform.runLater(() -> {
            this.isLoading.setValue(isLoading);
            if (isThumbnail) {
                this.loadingText.setValue("Generating thumbnail: " + quantityGenerated + "/" + totalQuantity);
            } else {
                this.loadingText.setValue("Generating top8: " + quantityGenerated + "/" + totalQuantity);
            }
        });
    }

    public void disableLoading() {
        update(false, true, 0, 0);
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
