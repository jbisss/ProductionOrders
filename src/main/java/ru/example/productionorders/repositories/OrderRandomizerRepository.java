package ru.example.productionorders.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.example.productionorders.dao.Customer;
import ru.example.productionorders.dao.Product;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class OrderRandomizerRepository {

    private final Connection connection;

    public OrderRandomizerRepository(ConnectionCredentials connectionCredentials) {
        connection = connectionCredentials.getConnection();
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String selectQuery = "SELECT * FROM public.\"Customers\";";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                customers.add(new Customer(rs.getString("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("Country"),
                        rs.getString("PostalCode")));
            }
        } catch (SQLException e) {
            log.error("Customers getting failure!", e);
        }
        return customers;
    }

    public List<Product> getProductsByCategorie(int id) {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM public.\"Products\" WHERE \"CategoryID\" = " + id + " ;";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                products.add(new Product(rs.getString("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("SupplierID"),
                        rs.getString("CategoryID"),
                        rs.getString("Unit"),
                        rs.getString("Price")));
            }
        } catch (SQLException e) {
            log.error("Products getting failure!", e);
        }
        return products;
    }
}
