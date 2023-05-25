package ru.example.productionorders.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.example.productionorders.classes.RandomCategorie;
import ru.example.productionorders.classes.RandomOrder;
import ru.example.productionorders.dao.*;
import ru.example.productionorders.configuration.ApplicationContextSingleton;
import ru.example.productionorders.repositories.ControllerRepository;
import ru.example.productionorders.serviceclasses.Cache;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;
import ru.example.productionorders.serviceclasses.OrderRandomizer;
import ru.example.productionorders.serviceclasses.WindowSwitcher;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
    public AnchorPane anchorInfo;
    public AnchorPane anchorButtons;
    public AnchorPane anchorTable;
    public AnchorPane anchorAdd;
    public AnchorPane anchorDelete;
    public Label labelTest;
    public ScrollPane ordersGeneratorScroll;
    public VBox orderGeneratorVBox;
    public VBox logVBox;
    public VBox doOrderVBox;
    public VBox shipperStatVBox;
    public TextArea textAreaLogsBack;
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

        setUpShipperLoad();

        Timer timer = new Timer();
        final OrderRandomizer orderRandomizer = context.getBean(OrderRandomizer.class);
        final boolean[] orderFlag = {true};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    RandomOrder randomOrder = null;
                    while (randomOrder == null) {
                        randomOrder = orderRandomizer.generateOrder();
                    }
                    TreeItem<String> customerNameTreeItem = new TreeItem<>("Customer: " + randomOrder.getCustomer().getCustomerName());
                    List<TreeItem<String>> categoriesNameTreeItems = new ArrayList<>();

                    for (RandomCategorie randomCategorie : randomOrder.getCategorieList()) {
                        TreeItem<String> categorieTreeItem = new TreeItem<>("Category: " + randomCategorie.getCategorie().getCategoryName());
                        List<TreeItem<String>> productsNameTreeItems = new ArrayList<>();
                        categoriesNameTreeItems.add(categorieTreeItem);
                        for (Product product : randomCategorie.getProductList()) {
                            TreeItem<String> productNameTreeItem = new TreeItem<>("Product: " + product.getProductName());
                            productsNameTreeItems.add(productNameTreeItem);
                        }
                        categorieTreeItem.getChildren().addAll(productsNameTreeItems);
                    }

                    customerNameTreeItem.getChildren().addAll(categoriesNameTreeItems);
                    TreeView<String> orderTreeView = new TreeView<>(customerNameTreeItem);
                    orderTreeView.setPrefHeight(150);
                    orderTreeView.setMinHeight(150);

                    VBox bufferVBox = new VBox();
                    bufferVBox.setSpacing(10);
                    bufferVBox.getChildren().add(orderTreeView);
                    // --- Reserve button EVENT
                    Button reserveOrderButton = new Button("Reserve order");
                    EventHandler<ActionEvent> reserveButtonClick = actionEvent -> {
                        if(orderFlag[0]) {
                            bufferVBox.getChildren().remove(reserveOrderButton);
                            orderGeneratorVBox.getChildren().remove(bufferVBox);
                            Label labelShipper = new Label("Choose shipper");
                            bufferVBox.getChildren().add(labelShipper);
                            ComboBox<String> shipperChoice = new ComboBox<>();
                            shipperChoice.setPromptText("Choose shipper");
                            shipperChoice.setPrefWidth(200);
                            Map<Shipper, Integer> shippers = context.getBean(ControllerRepository.class).getShipperLoad();
                            List<String> shipperName = shippers.keySet().stream().map(Shipper::getShipperName).toList();
                            shipperChoice.getItems().addAll(shipperName);
                            bufferVBox.getChildren().add(shipperChoice);
                            // --- Take order button EVENT
                            Button buttonTakeOrder = new Button("Take order");
                            EventHandler<ActionEvent> buttonTakeOrderClick = actionEvent1 -> {
                                doOrderVBox.getChildren().remove(bufferVBox);
                                orderFlag[0] = true;
                                textAreaLogsBack.setText(textAreaLogsBack.getText() + "\nOrder taken and added to DB!");
                            };
                            buttonTakeOrder.setOnAction(buttonTakeOrderClick);
                            bufferVBox.getChildren().add(buttonTakeOrder);
                            // --- Take order button EVENT
                            doOrderVBox.getChildren().add(bufferVBox);
                            orderFlag[0] = false;
                        } else {
                            log.info("Can't reserve!");
                        }
                    };
                    reserveOrderButton.setOnAction(reserveButtonClick);
                    // --- Reserve button EVENT
                    bufferVBox.getChildren().add(reserveOrderButton);
                    bufferVBox.getStyleClass().add("vbox-style");

                    orderGeneratorVBox.getChildren().add(bufferVBox);
                });
            }
        }, 0, 30000);
    }

    private void setStyles() {
        String vBoxStyleClass = "vbox-style";
        String rootStyleClass = "root";
        String anchorStyleClass = "anchor-style";
        String scrollStyle = "custom-scroll-pane";
        root.getStyleClass().add(rootStyleClass);
        mainVBox.getStyleClass().add(vBoxStyleClass);
        toolVBox.getStyleClass().add(vBoxStyleClass);
        buttonsVBox.getStyleClass().add(vBoxStyleClass);
        orderGeneratorVBox.getStyleClass().add(vBoxStyleClass);
        logVBox.getStyleClass().add(vBoxStyleClass);

        anchorAdd.getStyleClass().add(anchorStyleClass);
        anchorButtons.getStyleClass().add(anchorStyleClass);
        anchorTable.getStyleClass().add(anchorStyleClass);
        anchorInfo.getStyleClass().add(anchorStyleClass);
        anchorDelete.getStyleClass().add(anchorStyleClass);

        ordersGeneratorScroll.getStyleClass().add(scrollStyle);
    }

    public void exitButtonClick() {
        WindowSwitcher.closeWindow(exitButton, "/ru/example/productionorders/registration.fxml");
    }

    public void viewCustomersButtonClick() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        Image gifImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("travolta.gif")));
        ImageView imageView = new ImageView(gifImage);
        imageView.setFitWidth(195);
        imageView.setFitHeight(110);
        toolVBox.getChildren().add(imageView);
        toolVBox.setAlignment(Pos.CENTER);
        createTable(Customer.class);
    }

    public void viewCategoriesButtonClick() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Categorie.class);
        // configuring button click
        configureCategoryClick();
    }

    public void viewProductsButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Product.class);
        // configuring button click
        configureProductClick();
    }

    public void viewOrdersButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        Image gifImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("travolta.gif")));
        ImageView imageView = new ImageView(gifImage);
        imageView.setFitWidth(195);
        imageView.setFitHeight(110);
        toolVBox.getChildren().add(imageView);
        toolVBox.setAlignment(Pos.CENTER);
        createTable(Order.class);
        createTable(OrderDetail.class);
    }

    public void viewShippersButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Shipper.class);
        // configuring button click
        configureShipperClick();
    }

    public void viewSuppliersButton() {
        mainVBox.getChildren().clear();
        toolVBox.getChildren().clear();
        createTable(Supplier.class);
        // configuring button click
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
            if (!textFieldSupplierName.getText().matches("[A-Z][a-z]+ [A-Z]*[a-z]*")) {
                log.warn("Invalid supplier name");
                return;
            }
            Supplier transferSupplier = (Supplier) context.getBean("transferSupplier");
            transferSupplier.setSupplierID(String.valueOf(context.getBean(Cache.class).getSupplierMaxId() + 1));
            transferSupplier.setSupplierName(textFieldSupplierName.getText());
            transferSupplier.setContactName(textFieldContactName.getText());
            transferSupplier.setAddress(textFieldAddress.getText());
            transferSupplier.setCity(textFieldCity.getText());
            transferSupplier.setCountry(textFieldCountry.getText());
            transferSupplier.setPhone(textFieldPhone.getText());
            transferSupplier.setPostalCode(textFieldPostalCode.getText());
            context.getBean(ControllerRepository.class).addSupplierToDb();
            context.getBean(Cache.class).setSupplierMaxId(Integer.valueOf(transferSupplier.getSupplierID()));
            mainVBox.getChildren().clear();
            createTable(Supplier.class);
        };
        buttonAddSupplier.setOnAction(buttonAddSupplierClick);
        // --- Add supplier button EVENT

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

        // --- Add category button EVENT
        Button buttonAddCategory = new Button("Add category");
        EventHandler<ActionEvent> buttonAddCategoryClick = actionEvent -> {
            Categorie transferCategory = (Categorie) context.getBean("transferCategory");
            if (!textFieldCategoryName.getText().matches("[A-Z][a-z]+(/ )*[A-Z]*[a-z]*")) {
                log.warn("Invalid category name");
                return;
            }
            transferCategory.setCategoryID(String.valueOf(context.getBean(Cache.class).getCategoryMaxId() + 1));
            transferCategory.setCategoryName(textFieldCategoryName.getText());
            transferCategory.setDescription(textAreaCategoryDescription.getText());
            context.getBean(ControllerRepository.class).addCategoryToDb();
            context.getBean(Cache.class).setCategoryMaxId(Integer.valueOf(transferCategory.getCategoryID()));
            mainVBox.getChildren().clear();
            createTable(Categorie.class);
        };
        buttonAddCategory.setOnAction(buttonAddCategoryClick);
        // --- Add category button EVENT

        Label labelCategoryId = new Label("Enter category id to delete:");
        TextField textFieldCategoryIdToDelete = new TextField();

        // --- Delete category button EVENT
        Button buttonDeleteCategory = new Button("Delete");
        EventHandler<ActionEvent> buttonDeleteCategoryClick = actionEvent -> {
            int id = 0;
            try {
                id = Integer.parseInt(textFieldCategoryIdToDelete.getText());
            } catch (Exception e) {
                log.error("Invalid input format!");
            }
            context.getBean(ControllerRepository.class).deleteCategoryFromDb(id);
            mainVBox.getChildren().clear();
            textFieldCategoryIdToDelete.clear();
            createTable(Categorie.class);
        };
        buttonDeleteCategory.setOnAction(buttonDeleteCategoryClick);
        // --- Delete category button EVENT

        toolVBox.getChildren().addAll(labelCategoryName, textFieldCategoryName, labelCategoryDescription,
                textAreaCategoryDescription, buttonAddCategory, labelCategoryId, textFieldCategoryIdToDelete, buttonDeleteCategory);
    }

    /**
     * ПЕРЕСМОТРЕТЬ КЭШ - КОНЦЕПЦИЮ
     * При каждой конфигурации формировать кэш, а не использовать его постоянно
     * на всё время работы приложения
     */
    private void configureProductClick() {
        Cache cache = context.getBean(Cache.class);
        Label productNameLabel = new Label("Enter product name: ");
        TextField productNameField = new TextField();
        ComboBox<String> supplierChoice = new ComboBox<>();
        supplierChoice.setPromptText("Choose supplier");
        supplierChoice.setPrefWidth(200);
        for (String supplier : cache.getSupplierMap().keySet()) {
            supplierChoice.getItems().add(cache.getSupplierMap().get(supplier).getSupplierName());
        }
        ComboBox<String> categoryChoice = new ComboBox<>();
        categoryChoice.setPromptText("Choose category");
        categoryChoice.setPrefWidth(200);
        for (String categorie : cache.getCategorieMap().keySet()) {
            categoryChoice.getItems().add(cache.getCategorieMap().get(categorie).getCategoryName());
        }
        Label unitNameLabel = new Label("Enter unit: ");
        TextField unitNameField = new TextField();
        Label priceNameLabel = new Label("Enter price: ");
        TextField priceNameField = new TextField();

        // --- Add product button EVENT
        Button button = new Button("Add product");
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

        Label labelProductId = new Label("Enter product id to delete:");
        TextField textFieldProductId = new TextField();

        // --- Delete product button EVENT
        Button buttonProductDelete = new Button("Delete product");
        EventHandler<ActionEvent> buttonProductDeleteClick = actionEvent -> {
            int id = 0;
            try {
                id = Integer.parseInt(textFieldProductId.getText());
            } catch (Exception e) {
                log.error("Invalid input format!");
            }
            context.getBean(ControllerRepository.class).deleteProductFromDb(id);
            mainVBox.getChildren().clear();
            textFieldProductId.clear();
            createTable(Product.class);
        };
        buttonProductDelete.setOnAction(buttonProductDeleteClick);
        // --- Delete product button EVENT

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(productNameLabel, productNameField,
                supplierChoice,
                categoryChoice,
                unitNameLabel, unitNameField,
                priceNameLabel, priceNameField,
                button,
                labelProductId, textFieldProductId,
                buttonProductDelete);
        toolVBox.getChildren().add(vBox);
    }

    private void configureShipperClick() {
        Label labelShipperName = new Label("Enter shipper name to add:");
        TextField textFieldShipperName = new TextField();
        Label labelShipperPhone = new Label("Enter shipper phone to add:");
        TextField textFieldShipperPhone = new TextField();

        // --- Add shipper button EVENT
        Button buttonAddShipper = new Button("Add shipper");
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

        // --- Delete shipper button EVENT
        Button buttonDeleteShipper = new Button("Delete shipper");
        EventHandler<ActionEvent> buttonDeleteShipperClick = actionEvent -> {
            int id = 0;
            try {
                id = Integer.parseInt(textFieldShipperIdDelete.getText());
            } catch (Exception e) {
                log.error("Invalid input format!");
            }
            context.getBean(ControllerRepository.class).deleteShipperFromDb(id);
            mainVBox.getChildren().clear();
            createTable(Shipper.class);
        };
        buttonDeleteShipper.setOnAction(buttonDeleteShipperClick);
        // --- Delete shipper button EVENT

        toolVBox.getChildren().addAll(labelShipperIdDelete, textFieldShipperIdDelete, buttonDeleteShipper);
    }

    private void setUpShipperLoad() {
        Map<Shipper, Integer> shipperLoad = context.getBean(ControllerRepository.class).getShipperLoad();
        double totalOrders = 0;
        for(Shipper shipper : shipperLoad.keySet()) {
            totalOrders += shipperLoad.get(shipper);
        }
        for (Shipper shipper : shipperLoad.keySet()) {
            VBox bufferedVBox = new VBox();
            Label labelShipperId = new Label("Shipper id:");
            TextField textFieldShipperId = new TextField(shipper.getShipperID());
            Label labelShipperName = new Label("Shipper name:");
            TextField textFieldShipperName = new TextField(shipper.getShipperName());
            Label labelShipperPhone = new Label("Shipper phone");
            TextField textFieldShipperPhone = new TextField(shipper.getPhone());
            double percentage = shipperLoad.get(shipper) / totalOrders * 100;
            String stringPercentage = String.format("%.2f", percentage) + "%";
            Label labelLoadPercentage = new Label("Load percentage:");
            TextField textFieldLoadPercentage = new TextField(stringPercentage);
            bufferedVBox.getChildren().addAll(labelShipperId, textFieldShipperId,
                    labelShipperName, textFieldShipperName,
                    labelShipperPhone, textFieldShipperPhone,
                    labelLoadPercentage, textFieldLoadPercentage);
            bufferedVBox.getStyleClass().add("vbox-style");
            shipperStatVBox.getChildren().add(bufferedVBox);
        }
    }

    private <T> void createTable(Class<T> currentClass) {
        String selectQuery = "select * from \"" + currentClass.getSimpleName() + "s\";";
        if (currentClass.getSimpleName().equals("Order")) {
            selectQuery = "select * from \"Orders\" o join \"OrderDetails\" od " +
                    "on o.\"OrderID\" = od.\"OrderID\" where \"EmployeeID\" = " + context.getBean(Employee.class).getEmployeeId() + ";";
        }
        if (currentClass.getSimpleName().equals("OrderDetail")) {
            selectQuery = "select \"OrderDetailsID\", od.\"OrderID\", \"ProductID\", \"Quantity\" from \"Orders\" o join \"OrderDetails\" od " +
                    "on o.\"OrderID\" = od.\"OrderID\" where \"EmployeeID\" = " + context.getBean(Employee.class).getEmployeeId() + ";";
        }
        Field[] fields = currentClass.getDeclaredFields();
        ArrayList<String> fieldsName = new ArrayList<>();
        for (Field field : fields) {
            fieldsName.add(field.getName());
        }
        TableView<T> tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");
        for (String fieldName : fieldsName) {
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
                for (int i = 0; i < fieldsName.size(); i++) {
                    Field field = item.getClass().getDeclaredField(fieldsName.get(i));
                    field.setAccessible(true);
                    field.set(item, rs.getString(i + 1));
                }
                tableView.getItems().add(item);
                effectedRows++;
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException | NoSuchFieldException e) {
            log.error("Creating table failure!", e);
        }
        Label labelInfoRows = new Label("Effected rows: " + effectedRows);
        mainVBox.getChildren().add(tableView);
        mainVBox.getChildren().add(labelInfoRows);
    }
}
