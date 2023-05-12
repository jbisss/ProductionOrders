package ru.example.productionorders.configuration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextSingleton {

    private static AnnotationConfigApplicationContext context;

    public static AnnotationConfigApplicationContext getContext() {
        if (context == null) {
            context = new AnnotationConfigApplicationContext(MainConfiguration.class);
        }
        return context;
    }
}
