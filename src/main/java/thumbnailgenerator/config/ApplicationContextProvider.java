package thumbnailgenerator.config;

import org.springframework.context.ApplicationContext;

public class ApplicationContextProvider {

    private static ApplicationContext applicationContext;

    private ApplicationContextProvider() {}

    public static void setApplicationContext(ApplicationContext context) {
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
