package ru.example.productionorders.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.example.productionorders.classes.Customer;
import ru.example.productionorders.classes.Employee;
import ru.example.productionorders.configuration.ApplicationContextSingleton;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController {

    public TextField firstNameTextField;
    public TextField idText;
    public TextField lastNameTextField;
    public TextField birthTextField;
    public Button exitButton;
    public Button viewCustomersButton;
    public Button viewCategoriesButton;
    public Button viewProductsButton;
    public Button viewShippersButton;
    public Button viewSuppliersButton;
    public Button viewOrdersButton;
    public VBox mainVBox;

    public void initialize() {
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Employee currentEmployee = context.getBean(Employee.class);
        idText.setText(String.valueOf(currentEmployee.getEmployeeId()));
        firstNameTextField.setText(currentEmployee.getFirstName());
        lastNameTextField.setText(currentEmployee.getLastName());
        birthTextField.setText(currentEmployee.getBirthDate());
    }

    public void viewCustomersButtonClick() {
        TableView<Customer> tableView = new TableView<>();

        TableColumn<Customer, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        /*TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customer, String> cityColumn = new TableColumn<>("City");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        TableColumn<Customer, String> countryColumn = new TableColumn<>("Country");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        TableColumn<Customer, String> postalCodeColumn = new TableColumn<>("PostalCode");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));*/

        tableView.getColumns().add(idColumn);
        tableView.getColumns().add(nameColumn);
        /*tableView.getColumns().add(addressColumn);
        tableView.getColumns().add(cityColumn);
        tableView.getColumns().add(countryColumn);
        tableView.getColumns().add(postalCodeColumn);*/

        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Connection connection = context.getBean(ConnectionCredentials.class).getConnection();

        String selectCustomers = "select * from \"Customers\";";

        Statement statement;
        ResultSet rs;

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(selectCustomers);
            while(rs.next()) {
                Customer customer = new Customer(Integer.parseInt(rs.getString("CustomerId")),
                        rs.getString("CustomerName"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("Country"),
                        rs.getString("PostalCode"));
                tableView.getItems().add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        mainVBox.getChildren().add(tableView);
    }
}
