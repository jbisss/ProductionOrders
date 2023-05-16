package ru.example.productionorders.serviceclasses;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowSwitcher {

    public static void closeWindow(Button button, String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WindowSwitcher.class.getResource(fxmlPath));
        loaderFxml(loader);
        Stage stagePrev = (Stage) button.getScene().getWindow();
        stagePrev.hide();
    }

    private static void loaderFxml(FXMLLoader loader) {
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/ru/example/productionorders/simple.css");
        stage.setScene(scene);
        stage.setTitle("Main page");
        stage.show();
    }
}
