package thumbnailgenerator.config;

import org.springframework.context.ApplicationContext;

public class ApplicationContextProvider {

    private static ApplicationContext applicationContext;

    private ApplicationContextProvider() {}

    public static void setApplicationContext(ApplicationContext context) {
        if (applicationContext == null) {
            applicationContext = context;
        } else {
            throw new IllegalStateException("ApplicationContext is already initialized");
        }
    }

    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext has not been initialized yet");
        }
        return applicationContext;
    }
}
