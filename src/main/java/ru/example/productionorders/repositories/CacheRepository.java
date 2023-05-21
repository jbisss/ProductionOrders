package ru.example.productionorders.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.example.productionorders.classes.Categorie;
import ru.example.productionorders.classes.Supplier;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class CacheRepository {

    private final Connection connection;

    public CacheRepository(ConnectionCredentials connectionCredentials) {
        this.connection = connectionCredentials.getConnection();
    }

    public Map<String, Supplier> getSuppliers() {
        Map<String, Supplier> result = new HashMap<>();
        String querySuppliers = "select * from \"Suppliers\";";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(querySuppliers);
            while (rs.next()) {
                result.put(rs.getString("SupplierName"), new Supplier(rs.getString("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("ContactName"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("Country"),
                        rs.getString("Phone"),
                        rs.getString("PostalCode")));
            }
            log.info("Suppliers successfully cached!");
        } catch (SQLException e) {
            log.error("Cache creation failure!", e);
        }
        return result;
    }

    public Map<String, Categorie> getCategories() {
        Map<String, Categorie> result = new HashMap<>();
        String queryCategories = "select * from \"Categories\";";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(queryCategories);
            while (rs.next()) {
                result.put(rs.getString("CategoryName"), new Categorie(rs.getString("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")));
            }
            log.info("Categories successfully cached!");
        } catch (SQLException e) {
            log.error("Cache creation failure!", e);
        }
        return result;
    }

    public int getMaxProductId() {
        int result = 0;
        String queryMaxProductId = "select max(\"ProductID\") from \"Products\";";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(queryMaxProductId);
            rs.next();
            result = rs.getInt(1);
            log.info("Product maxId = {} successfully cached!", result);
        } catch (SQLException e) {
            log.error("Cache creation failure!", e);
        }
        return result;
    }

    public int getMaxShipperId() {
        int result = 0;
        String queryMaxShipperId = "select max(\"ShipperID\") from \"Shippers\";";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(queryMaxShipperId);
            rs.next();
            result = rs.getInt(1);
            log.info("Shipper maxId = {} successfully cached!", result);
        } catch (SQLException e) {
            log.error("Cache creation failure!", e);
        }
        return result;
    }

    public int getMaxCategoryId() {
        int result = 0;
        String queryMaxCategoryId = "select max(\"CategoryID\") from \"Categories\";";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(queryMaxCategoryId);
            rs.next();
            result = rs.getInt(1);
            log.info("Category maxId = {} successfully cached!", result);
        } catch (SQLException e) {
            log.error("Cache creation failure!", e);
        }
        return result;
    }

    public int getMaxSupplierId() {
        int result = 0;
        String queryMaxCategoryId = "select max(\"SupplierID\") from \"Suppliers\";";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(queryMaxCategoryId);
            rs.next();
            result = rs.getInt(1);
            log.info("Supplier maxId = {} successfully cached!", result);
        } catch (SQLException e) {
            log.error("Cache creation failure!", e);
        }
        return result;
    }
}
