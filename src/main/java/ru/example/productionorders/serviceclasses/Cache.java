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
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
@Slf4j
public class Cache {

    private Map<String, Supplier> supplierMap = new HashMap<>();
    private Map<String, Categorie> categorieMap = new HashMap<>();
    private int productMaxId;

    public void initCache() {
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        Connection connection = context.getBean(ConnectionCredentials.class).getConnection();
        String querySuppliers = "select * from \"Suppliers\";";
        String queryCategories = "select * from \"Categories\";";
        String queryMaxProductId = "select max(\"ProductID\") from \"Products\";";

        Statement statement;
        ResultSet rs;

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(querySuppliers);
            while (rs.next()) {
                supplierMap.put(rs.getString("SupplierName"), new Supplier(rs.getString("SupplierID"),
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
                categorieMap.put(rs.getString("CategoryName"), new Categorie(rs.getString("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")));
            }
            log.info("Categories successfully cached!");
            rs = statement.executeQuery(queryMaxProductId);
            rs.next();
            productMaxId = rs.getInt(1);
            log.info("Cached created successfully!");
        } catch (SQLException e) {
            log.error("Cache creation failure!", e);
        }
    }
}
