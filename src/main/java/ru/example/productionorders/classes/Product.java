package ru.example.productionorders.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product {

    private String ProductId;
    private String ProductName;
    private String SupplierID;
    private String CategoryID;
    private String Unit;
    private String Price;
}
