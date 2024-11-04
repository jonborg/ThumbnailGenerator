package thumbnailgenerator.config;

import javafx.fxml.FXMLLoader;

public class SpringFXMLLoader extends FXMLLoader {

    public SpringFXMLLoader(String fxmlPath) {
        super();
        this.setLocation(getClass().getClassLoader().getResource(fxmlPath));
        this.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
    }
}
