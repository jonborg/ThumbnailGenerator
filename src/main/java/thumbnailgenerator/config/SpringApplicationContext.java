package thumbnailgenerator.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import thumbnailgenerator.Main;

public class SpringApplicationContext {
    private static ApplicationContext context;

    static {
        context = new SpringApplicationBuilder(Main.class).run();
    }

    public static ApplicationContext getContext() {
        return context;
    }
}

