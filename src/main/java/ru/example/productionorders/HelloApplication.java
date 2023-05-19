package ru.example.productionorders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.example.productionorders.configuration.ApplicationContextSingleton;
import ru.example.productionorders.serviceclasses.Cache;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("/ru/example/productionorders/simple.css");
        stage.setTitle("Registration");
        stage.setScene(scene);
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        context.getBean(Cache.class).initCache();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}