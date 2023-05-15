package ru.example.productionorders.controllers;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.example.productionorders.classes.*;
import ru.example.productionorders.configuration.ApplicationContextSingleton;
import ru.example.productionorders.serviceclasses.Cache;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;
import ru.example.productionorders.serviceclasses.WindowSwitcher;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Slf4j
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
    public VBox toolVBox;

    public void initialize() {
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Employee currentEmployee = context.getBean(Employee.class);
        idText.setText(String.valueOf(currentEmployee.getEmployeeId()));
        firstNameTextField.setText(currentEmployee.getFirstName());
        lastNameTextField.setText(currentEmployee.getLastName());
        birthTextField.setText(currentEmployee.getBirthDate());
    }

    public void exitButtonClick() {
        WindowSwitcher.closeWindow(exitButton, "/ru/example/productionorders/registration.fxml");
    }

    public void viewCustomersButtonClick() {
        mainVBox.getChildren().clear();
        createTable(Customer.class);
    }

    public void viewCategoriesButtonClick() {
        mainVBox.getChildren().clear();
        createTable(Categorie.class);
    }

    public void viewProductsButton() {
        mainVBox.getChildren().clear();
        createTable(Product.class);
        configureProductClick();
    }

    public void viewOrdersButton() {
        mainVBox.getChildren().clear();
        createTable(Order.class);
        createTable(OrderDetail.class);
    }

    public void viewShippersButton() {
        mainVBox.getChildren().clear();
        createTable(Shipper.class);
    }

    public void viewSuppliersButton() {
        mainVBox.getChildren().clear();
        createTable(Supplier.class);
    }

    private void configureProductClick() {
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Cache cache = context.getBean(Cache.class);
        Label productNameLabel = new Label("Enter product name: ");
        TextField productNameField = new TextField();
        ComboBox<String> supplierChoice = new ComboBox<>();
        supplierChoice.setPromptText("Choose supplier");
        for(Supplier supplier : cache.getSupplierList()) {
            supplierChoice.getItems().add(supplier.getSupplierName());
        }
        ComboBox<String> categoryChoice = new ComboBox<>();
        categoryChoice.setPromptText("Choose category");
        for(Categorie categorie : cache.getCategorieList()) {
            categoryChoice.getItems().add(categorie.getCategoryName());
        }
        Label unitNameLabel = new Label("Enter unit: ");
        TextField unitNameField = new TextField();
        Label priceNameLabel = new Label("Enter price: ");
        TextField priceNameField = new TextField();
        Button button = new Button("Add product");
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(productNameLabel, productNameField,
                supplierChoice,
                categoryChoice,
                unitNameLabel, unitNameField,
                priceNameLabel, priceNameField,
                button);
        toolVBox.getChildren().add(vBox);
    }

    private <T> void createTable(Class<T> currentClass) {
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        String selectQuery = "select * from \"" + currentClass.getSimpleName() + "s\";";
        if(currentClass.getSimpleName().equals("Order")) {
            selectQuery = "select * from \"Orders\" o join \"OrderDetails\" od " +
                    "on o.\"OrderID\" = od.\"OrderID\" where \"EmployeeID\" = " + context.getBean(Employee.class).getEmployeeId() + ";";
        }
        if(currentClass.getSimpleName().equals("OrderDetail")) {
            selectQuery = "select \"OrderDetailsID\", od.\"OrderID\", \"ProductID\", \"Quantity\" from \"Orders\" o join \"OrderDetails\" od " +
                    "on o.\"OrderID\" = od.\"OrderID\" where \"EmployeeID\" = " + context.getBean(Employee.class).getEmployeeId() + ";";
        }
        Field[] fields = currentClass.getDeclaredFields();
        ArrayList<String> fieldsName = new ArrayList<>();
        for(Field field : fields) {
            fieldsName.add(field.getName());
        }
        TableView<T> tableView = new TableView<>();
        for(String fieldName : fieldsName) {
            TableColumn<T, String> newColumn = new TableColumn<>(fieldName);
            newColumn.setCellValueFactory(new PropertyValueFactory<>(fieldName));
            tableView.getColumns().add(newColumn);
        }

        Connection connection = context.getBean(ConnectionCredentials.class).getConnection();

        Statement statement;
        ResultSet rs;

        int effectedRows = 0;

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                T item = currentClass.getDeclaredConstructor().newInstance();
                for(int i = 0; i < fieldsName.size(); i++) {
                    Field field = item.getClass().getDeclaredField(fieldsName.get(i));
                    field.setAccessible(true);
                    field.set(item, rs.getString(i + 1));
                }
                tableView.getItems().add(item);
                effectedRows++;
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException e) {
           log.error("Creating table failure!", e);
        }
        Label labelInfoRows = new Label("Effected rows: " + effectedRows);
        mainVBox.getChildren().add(tableView);
        mainVBox.getChildren().add(labelInfoRows);
    }
}
