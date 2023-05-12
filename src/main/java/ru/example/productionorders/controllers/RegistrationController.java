package ru.example.productionorders.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import ru.example.productionorders.classes.Employee;
import ru.example.productionorders.configuration.ApplicationContextSingleton;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;

import java.io.IOException;
import java.sql.*;

@Controller
@Slf4j
public class RegistrationController {

    public TextField loginTextField;
    public TextField passwordTextField;
    public Button enterButton;

    @FXML
    protected void enterButtonClick() {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Connection connection = context.getBean(ConnectionCredentials.class).getConnection();
        String selectEmployee = "select * from \"Employees\" where \"FirstName\" = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectEmployee)) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                log.warn("There is no such an employee!!!");
            } else {
                if (password.equals(rs.getString("LastName"))) {
                    log.info("Success!!!");
                    Employee currentEmployee = context.getBean(Employee.class);
                    currentEmployee.setEmployeeId(Integer.parseInt(rs.getString("EmployeeId")));
                    currentEmployee.setFirstName(rs.getString("FirstName"));
                    currentEmployee.setLastName(rs.getString("LastName"));
                    currentEmployee.setBirthDate(rs.getString("BirthDate"));
                    currentEmployee.setNotes(rs.getString("Notes"));
                    closeWindow(enterButton);
                } else {
                    log.warn("Invalid password!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow(Button button) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("main.fxml"));
        LoaderFxml(loader);
        Stage stagePrev = (Stage) button.getScene().getWindow();
        stagePrev.hide();
    }

    private void LoaderFxml(FXMLLoader loader) {
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Main page");
        stage.show();
    }
}