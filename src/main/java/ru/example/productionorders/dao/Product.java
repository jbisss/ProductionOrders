package ru.example.productionorders.dao;

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

    public Product(){}

    public Product(String productId, String productName, String supplierID, String categoryID, String unit, String price) {
        ProductId = productId;
        ProductName = productName;
        SupplierID = supplierID;
        CategoryID = categoryID;
        Unit = unit;
        Price = price;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null || other.getClass() != this.getClass()){
            return false;
        }
        return this.ProductId.equals(((Product) other).getProductId());
    }

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
