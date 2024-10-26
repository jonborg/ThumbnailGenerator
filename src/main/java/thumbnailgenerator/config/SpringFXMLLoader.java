package thumbnailgenerator.config;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;

public class SpringFXMLLoader extends FXMLLoader {
    private final ApplicationContext context;

    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    public void loadFile(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlPath));
        loader.setControllerFactory(context::getBean);
    }
}
