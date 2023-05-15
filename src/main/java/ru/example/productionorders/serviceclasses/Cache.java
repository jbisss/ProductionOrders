package ru.example.productionorders.serviceclasses;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.example.productionorders.classes.Categorie;
import ru.example.productionorders.classes.Supplier;
import ru.example.productionorders.configuration.ApplicationContextSingleton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@Slf4j
public class Cache {

    private List<Supplier> supplierList = new ArrayList<>();
    private List<Categorie> categorieList = new ArrayList<>();

    public void initCache() {
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Connection connection = context.getBean(ConnectionCredentials.class).getConnection();
        String querySuppliers = "select * from \"Suppliers\";";
        String queryCategories = "select * from \"Categories\";";

        Statement statement;
        ResultSet rs;

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(querySuppliers);
            while (rs.next()) {
                supplierList.add(new Supplier(rs.getString("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("ContactName"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("Country"),
                        rs.getString("Phone"),
                        rs.getString("PostalCode")));
            }
            log.info("Suppliers successfully cached!");
            rs = statement.executeQuery(queryCategories);
            while (rs.next()) {
                categorieList.add(new Categorie(rs.getString("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")));
            }
            log.info("Categories successfully cached!");
            log.info("Cached created successfully!");
        } catch (SQLException e) {
            log.error("Cache creation failure!");
        }
    }
}
