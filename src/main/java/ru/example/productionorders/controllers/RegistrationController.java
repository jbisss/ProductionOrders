package ru.example.productionorders.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import ru.example.productionorders.classes.Employee;
import ru.example.productionorders.configuration.ApplicationContextSingleton;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;
import ru.example.productionorders.serviceclasses.WindowSwitcher;

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
                    Employee currentEmployee = context.getBean(Employee.class);
                    currentEmployee.setEmployeeId(Integer.parseInt(rs.getString("EmployeeId")));
                    currentEmployee.setFirstName(rs.getString("FirstName"));
                    currentEmployee.setLastName(rs.getString("LastName"));
                    currentEmployee.setBirthDate(rs.getString("BirthDate"));
                    currentEmployee.setNotes(rs.getString("Notes"));
                    WindowSwitcher.closeWindow(enterButton, "/ru/example/productionorders/controllers/main.fxml");
                    log.info("Success registration!!!");
                } else {
                    log.warn("Invalid password!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}