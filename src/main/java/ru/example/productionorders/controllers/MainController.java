package ru.example.productionorders.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    public VBox buttonsVBox;
    public AnchorPane root;

    public void initialize() {
        setStyles();
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Employee currentEmployee = context.getBean(Employee.class);
        idText.setText(String.valueOf(currentEmployee.getEmployeeId()));
        firstNameTextField.setText(currentEmployee.getFirstName());
        lastNameTextField.setText(currentEmployee.getLastName());
        birthTextField.setText(currentEmployee.getBirthDate());
    }

    private void setStyles() {
        String vBoxStyleClass = "vbox-style";
        String buttonStyleClass = "button-style";
        String rootStyleClass = "root";
        root.getStyleClass().add(rootStyleClass);
        mainVBox.getStyleClass().add(vBoxStyleClass);
        toolVBox.getStyleClass().add(vBoxStyleClass);
        buttonsVBox.getStyleClass().add(vBoxStyleClass);
        exitButton.getStyleClass().add(buttonStyleClass);
        viewCustomersButton.getStyleClass().add(buttonStyleClass);
        viewCategoriesButton.getStyleClass().add(buttonStyleClass);
        viewProductsButton.getStyleClass().add(buttonStyleClass);
        viewShippersButton.getStyleClass().add(buttonStyleClass);
        viewSuppliersButton.getStyleClass().add(buttonStyleClass);
        viewOrdersButton.getStyleClass().add(buttonStyleClass);
    }

    public void exitButtonClick() {
        WindowSwitcher.closeWindow(exitButton, "/ru/example/productionorders/registration.fxml");
    }

    public void viewCustomersButtonClick() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Customer.class);
    }

    public void viewCategoriesButtonClick() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Categorie.class);
    }

    public void viewProductsButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Product.class);
        configureProductClick();
    }

    public void viewOrdersButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Order.class);
        createTable(OrderDetail.class);
    }

    public void viewShippersButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Shipper.class);
    }

    public void viewSuppliersButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Supplier.class);
    }

    private void configureProductClick() {
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Cache cache = context.getBean(Cache.class);
        Label productNameLabel = new Label("Enter product name: ");
        TextField productNameField = new TextField();
        ComboBox<String> supplierChoice = new ComboBox<>();
        supplierChoice.setPromptText("Choose supplier");
        for(String supplier : cache.getSupplierMap().keySet()) {
            supplierChoice.getItems().add(cache.getSupplierMap().get(supplier).getSupplierName());
        }
        ComboBox<String> categoryChoice = new ComboBox<>();
        categoryChoice.setPromptText("Choose category");
        for(String categorie : cache.getCategorieMap().keySet()) {
            categoryChoice.getItems().add(cache.getCategorieMap().get(categorie).getCategoryName());
        }
        Label unitNameLabel = new Label("Enter unit: ");
        TextField unitNameField = new TextField();
        Label priceNameLabel = new Label("Enter price: ");
        TextField priceNameField = new TextField();
        Button button = new Button("Add product");
        // --- Add product button EVENT
        EventHandler<ActionEvent> addProductButtonClick = actionEvent -> {
            if (productNameField.getText().isEmpty()) {
                log.error("Product name is empty!");
                return;
            }
            if (supplierChoice.getValue() == null) {
                log.error("Supplier is empty!");
                return;
            }
            if (categoryChoice.getValue() == null) {
                log.error("Category is empty!");
                return;
            }
            if (unitNameField.getText().isEmpty()) {
                log.error("Unit name is empty!");
                return;
            }
            if (priceNameField.getText().isEmpty()) {
                log.error("Price is empty!");
                return;
            }
            try {
                double priceToSet = Double.parseDouble(priceNameField.getText());
                Product transferProduct = (Product) context.getBean("transferProduct");
                transferProduct.setProductId(String.valueOf(context.getBean(Cache.class).getProductMaxId() + 1));
                transferProduct.setProductName(productNameField.getText());
                transferProduct.setSupplierID(cache.getSupplierMap().get(supplierChoice.getValue()).getSupplierID());
                transferProduct.setCategoryID(cache.getCategorieMap().get(categoryChoice.getValue()).getCategoryID());
                transferProduct.setUnit(unitNameField.getText());
                transferProduct.setPrice(String.valueOf(priceToSet));
                log.info(transferProduct.toString());
            } catch (NumberFormatException e) {
                log.error("Invalid price input!");
            }
        };
        // --- Add product button EVENT
        button.setOnAction(addProductButtonClick);
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
        tableView.getStyleClass().add("table-view");
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
