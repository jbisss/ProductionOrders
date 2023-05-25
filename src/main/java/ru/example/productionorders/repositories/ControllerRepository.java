package ru.example.productionorders.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.example.productionorders.dao.Categorie;
import ru.example.productionorders.dao.Product;
import ru.example.productionorders.dao.Shipper;
import ru.example.productionorders.dao.Supplier;
import ru.example.productionorders.serviceclasses.Cache;
import ru.example.productionorders.serviceclasses.ConnectionCredentials;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class ControllerRepository {

    private final Connection connection;
    private final Cache cache;
    private final Shipper transferShipper;
    private final Product transferProduct;
    private final Categorie transferCategory;
    private final Supplier transferSupplier;

    public ControllerRepository(ConnectionCredentials connectionCredentials, Cache cache, Shipper transferShipper, Product transferProduct, Categorie transferCategory, Supplier transferSupplier) {
        connection = connectionCredentials.getConnection();
        this.cache = cache;
        this.transferShipper = transferShipper;
        this.transferProduct = transferProduct;
        this.transferCategory = transferCategory;
        this.transferSupplier = transferSupplier;
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
        cache.getCategorieMap().put(transferCategory.getCategoryName(), transferCategory);
    }

    public void deleteCategoryFromDb(int id) {
        String deleteQuery = "DELETE FROM public.\"Categories\" WHERE \"CategoryID\" = " + id + ";";
        log.info("Query started: " + deleteQuery);
        try (Statement statement = connection.createStatement()) {
            int rowsDeleted = statement.executeUpdate(deleteQuery);
            log.info("Rows deleted: {}", rowsDeleted);
        } catch (SQLException e) {
            log.error("Statement failed!!!");
        }
        cache.getCategorieMap().remove(transferCategory.getCategoryName());
    }

    public void addSupplierToDb() {
        String insertQuery = "INSERT INTO public.\"Suppliers\"(\n" +
                "\t\"SupplierID\", \"SupplierName\", \"ContactName\", \"Address\", \"City\", \"Country\", \"Phone\", \"PostalCode\")\n" +
                "\tVALUES " + transferSupplier + ";";
        log.info("Query started: " + insertQuery);
        try (Statement statement = connection.createStatement()) {
            int rowsInserted = statement.executeUpdate(insertQuery);
            log.info("Rows inserted: {}", rowsInserted);
        } catch (SQLException e) {
            log.error("Statement failed!!!");
        }
        cache.getSupplierMap().put(transferSupplier.getSupplierName(), transferSupplier);
    }

    public Map<Shipper, Integer> getShipperLoad() {
        Map<Shipper, Integer> shipperLoadResult = new HashMap<>();
        String selectQuery = "select \"ShipperID\", COUNT(*) from \"Orders\"\n" +
                " group by \"ShipperID\";";
        log.info("Query started: " + selectQuery);
        ResultSet rs_1;
        try (Statement statement_1 = connection.createStatement()) {
            rs_1 = statement_1.executeQuery(selectQuery);
            while (rs_1.next()) {
                String shipperId = rs_1.getString("ShipperID");
                int count = Integer.parseInt(rs_1.getString("count"));
                String selectShipper = "select * from \"Shippers\" where \"ShipperID\" = " + shipperId + ";";
                Statement statement_2 = connection.createStatement();
                ResultSet rs_2;
                rs_2 = statement_2.executeQuery(selectShipper);
                Shipper shipperToAdd = null;
                while(rs_2.next()) {
                    shipperToAdd = new Shipper(rs_2.getString("ShipperID"),
                            rs_2.getString("ShipperName"),
                            rs_2.getString("Phone"));
                }
                shipperLoadResult.put(shipperToAdd, count);
            }
        } catch (SQLException e) {
            log.error("Statement failed!!!", e);
        }
        return shipperLoadResult;
    }
}
