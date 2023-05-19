package ru.example.productionorders.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.example.productionorders.classes.Categorie;
import ru.example.productionorders.classes.Product;
import ru.example.productionorders.classes.Shipper;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
@Slf4j
public class ControllerRepository {

    private final Connection connection;
    private final Shipper transferShipper;
    private final Product transferProduct;
    private final Categorie transferCategory;

    public ControllerRepository(ConnectionCredentials connectionCredentials, Shipper transferShipper, Product transferProduct, Categorie transferCategory) {
        connection = connectionCredentials.getConnection();
        this.transferShipper = transferShipper;
        this.transferProduct = transferProduct;
        this.transferCategory = transferCategory;
    }

    public void addShipperToDb() {
        String insertQuery = "INSERT INTO public.\"Shippers\"(\n" +
                "\t\"ShipperID\", \"ShipperName\", \"Phone\")\n" +
                "\tVALUES " + transferShipper + ";";
        try (Statement statement = connection.createStatement()) {
            int rowsInserted = statement.executeUpdate(insertQuery);
            log.info("Rows inserted: {}", rowsInserted);
        } catch (SQLException e) {
            log.error("Statement failed!!!");
        }
    }

    public void deleteShipperFromDb(int id) {
        String deleteQuery = "DELETE FROM public.\"Shippers\" WHERE \"ShipperID\" = " + id + ";";
        log.info("Query started: " + deleteQuery);
        try (Statement statement = connection.createStatement()) {
            int rowsDeleted = statement.executeUpdate(deleteQuery);
            log.info("Rows deleted: {}", rowsDeleted);
        } catch (SQLException e) {
            log.error("Statement failed!!!");
        }
    }

    public void addProductToDb() {
        String insertQuery = "INSERT INTO public.\"Products\"(\n" +
                "\t\"ProductID\", \"ProductName\", \"SupplierID\", \"CategoryID\", \"Unit\", \"Price\")\n" +
                "\tVALUES " + transferProduct + ";";
        log.info("Query started: " + insertQuery);
        try (Statement statement = connection.createStatement()) {
            int rowsInserted = statement.executeUpdate(insertQuery);
            log.info("Rows inserted: {}", rowsInserted);
        } catch (SQLException e) {
            log.error("Statement failed!!!");
        }
    }

    public void deleteProductFromDb(int id) {
        String deleteQuery = "DELETE FROM public.\"Products\" WHERE \"ProductID\" = " + id + ";";
        log.info("Query started: " + deleteQuery);
        try (Statement statement = connection.createStatement()) {
            int rowsDeleted = statement.executeUpdate(deleteQuery);
            log.info("Rows deleted: {}", rowsDeleted);
        } catch (SQLException e) {
            log.error("Statement failed!!!");
        }
    }

    public void addCategoryToDb() {
        String insertQuery = "INSERT INTO public.\"Categories\"(\n" +
                "\t\"CategoryID\", \"CategoryName\", \"Description\")\n" +
                "\tVALUES " + transferCategory + ";";
        log.info("Query started: " + insertQuery);
        try (Statement statement = connection.createStatement()) {
            int rowsInserted = statement.executeUpdate(insertQuery);
            log.info("Rows inserted: {}", rowsInserted);
        } catch (SQLException e) {
            log.error("Statement failed!!!");
        }
    }
}
