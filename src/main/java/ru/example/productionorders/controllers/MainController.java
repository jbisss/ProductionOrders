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
import ru.example.productionorders.repositories.ControllerRepository;
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
    public Label firstNameLabel;
    public Label employeeIdLabel;
    public Label lastNameLabel;
    public Label birthDateLabel;
    private AnnotationConfigApplicationContext context;
    private Connection connection;

    public void initialize() {
        setStyles();
        context = ApplicationContextSingleton.getContext();
        connection = context.getBean(ConnectionCredentials.class).getConnection();
        Employee currentEmployee = context.getBean(Employee.class);
        idText.setText(String.valueOf(currentEmployee.getEmployeeId()));
        firstNameTextField.setText(currentEmployee.getFirstName());
        lastNameTextField.setText(currentEmployee.getLastName());
        birthTextField.setText(currentEmployee.getBirthDate());
    }

    private void setStyles() {
        String vBoxStyleClass = "vbox-style";
        String rootStyleClass = "root";
        root.getStyleClass().add(rootStyleClass);
        mainVBox.getStyleClass().add(vBoxStyleClass);
        toolVBox.getStyleClass().add(vBoxStyleClass);
        buttonsVBox.getStyleClass().add(vBoxStyleClass);
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
        configureCategoryClick();
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
        configureShipperClick();
    }

    public void viewSuppliersButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Supplier.class);
        configureSupplierClick();
    }

    private void configureSupplierClick() {
        Label labelSupplierName = new Label("Enter supplier name:");
        TextField textFieldSupplierName = new TextField();
        Label labelContactName = new Label("Enter contact name:");
        TextField textFieldContactName = new TextField();
        Label labelAddress = new Label("Enter address:");
        TextField textFieldAddress = new TextField();
        Label labelCity = new Label("Enter city:");
        TextField textFieldCity = new TextField();
        Label labelCountry = new Label("Enter country:");
        TextField textFieldCountry = new TextField();
        Label labelPhone = new Label("Enter phone:");
        TextField textFieldPhone = new TextField();
        Label labelPostalCode = new Label("Enter postal code:");
        TextField textFieldPostalCode = new TextField();

        Button buttonAddSupplier = new Button("Add supplier");
        // --- Add supplier button EVENT
        EventHandler<ActionEvent> buttonAddSupplierClick = actionEvent -> {

        };
        buttonAddSupplier.setOnAction(buttonAddSupplierClick);
        // --- Add category button EVENT
        toolVBox.getChildren().addAll();
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setSpacing(25);
        vBox.getStyleClass().add("vbox-style");
        vBox.getChildren().addAll(labelSupplierName, textFieldSupplierName, labelContactName, textFieldContactName, labelAddress,
                textFieldAddress, labelCity, textFieldCity, labelCountry, textFieldCountry, labelPhone, textFieldPhone, labelPostalCode, textFieldPostalCode, buttonAddSupplier);
        scrollPane.setContent(vBox);
        toolVBox.getChildren().add(scrollPane);
    }

    private void configureCategoryClick() {
        Label labelCategoryName = new Label("Enter category name:");
        TextField textFieldCategoryName = new TextField();
        Label labelCategoryDescription = new Label("Enter category description");
        TextArea textAreaCategoryDescription = new TextArea();

        Button buttonAddCategory = new Button("Add category");
        // --- Add category button EVENT
        EventHandler<ActionEvent> buttonAddCategoryClick = actionEvent -> {

        };
        buttonAddCategory.setOnAction(buttonAddCategoryClick);
        // --- Add category button EVENT

        toolVBox.getChildren().addAll(labelCategoryName, textFieldCategoryName, labelCategoryDescription, textAreaCategoryDescription, buttonAddCategory);
    }

    private void configureProductClick() {
        Cache cache = context.getBean(Cache.class);
        Label productNameLabel = new Label("Enter product name: ");
        TextField productNameField = new TextField();
        ComboBox<String> supplierChoice = new ComboBox<>();
        supplierChoice.setPromptText("Choose supplier");
        supplierChoice.setPrefWidth(200);
        for(String supplier : cache.getSupplierMap().keySet()) {
            supplierChoice.getItems().add(cache.getSupplierMap().get(supplier).getSupplierName());
        }
        ComboBox<String> categoryChoice = new ComboBox<>();
        categoryChoice.setPromptText("Choose category");
        categoryChoice.setPrefWidth(200);
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
                context.getBean(ControllerRepository.class).addProductToDb();
                mainVBox.getChildren().clear();
                createTable(Product.class);
                context.getBean(Cache.class).setProductMaxId(Integer.parseInt(transferProduct.getProductId()));
            } catch (NumberFormatException e) {
                log.error("Invalid price input!");
            }
        };
        button.setOnAction(addProductButtonClick);
        // --- Add product button EVENT
        
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

    private void configureShipperClick() {
        Label labelShipperName = new Label("Enter shipper name to add:");
        TextField textFieldShipperName = new TextField();
        Label labelShipperPhone = new Label("Enter shipper phone to add:");
        TextField textFieldShipperPhone = new TextField();

        Button buttonAddShipper = new Button("Add shipper");
        // --- Add shipper button EVENT
        EventHandler<ActionEvent> buttonAddShipperClick = actionEvent -> {
            String shipperName = textFieldShipperName.getText();
            String shipperPhone = textFieldShipperPhone.getText();
            if (!shipperName.matches("[A-Z][a-z]+ [A-Z][a-z]+")) {
                log.error("Invalid name for shipper!!!");
                return;
            }
            if (!shipperPhone.matches("\\(\\d{3}\\) \\d{3}-\\d{4}")) {
                log.error("Invalid phone number for shipper!!!");
                return;
            }
            Shipper transferShipper = (Shipper) ApplicationContextSingleton.getContext().getBean("transferShipper");
            transferShipper.setShipperID(String.valueOf(context.getBean(Cache.class).getShipperMaxId() + 1));
            transferShipper.setShipperName(shipperName);
            transferShipper.setPhone(shipperPhone);
            log.info(transferShipper.toString());
            context.getBean(ControllerRepository.class).addShipperToDb();
            textFieldShipperName.clear();
            textFieldShipperPhone.clear();
            mainVBox.getChildren().clear();
            createTable(Shipper.class);
            context.getBean(Cache.class).setShipperMaxId(Integer.parseInt(transferShipper.getShipperID()));
        };
        buttonAddShipper.setOnAction(buttonAddShipperClick);
        // --- Add shipper button EVENT

        toolVBox.getChildren().addAll(labelShipperName, textFieldShipperName, labelShipperPhone, textFieldShipperPhone, buttonAddShipper);
        Label labelShipperIdDelete = new Label("Enter shipper id to delete:");
        TextField textFieldShipperIdDelete = new TextField();

        Button buttonDeleteShipper = new Button("Delete shipper");
        // --- Delete shipper button EVENT
        EventHandler<ActionEvent> buttonDeleteShipperClick = actionEvent -> {

        };
        buttonDeleteShipper.setOnAction(buttonDeleteShipperClick);
        // --- Delete shipper button EVENT

        toolVBox.getChildren().addAll(labelShipperIdDelete, textFieldShipperIdDelete, buttonDeleteShipper);
    }

    private <T> void createTable(Class<T> currentClass) {
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
