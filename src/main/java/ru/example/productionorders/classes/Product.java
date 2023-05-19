package ru.example.productionorders.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private String ProductId;
    private String ProductName;
    private String SupplierID;
    private String CategoryID;
    private String Unit;
    private String Price;

    @Override
    public String toString() {
        return "(" +
                ProductId + ", " +
                "'" + ProductName + "', " +
                SupplierID + ", " +
                CategoryID + ", " +
                "'" + Unit + "'," +
                Price +
                ")";
    }
}
