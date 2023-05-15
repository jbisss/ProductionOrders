package ru.example.productionorders.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetail {

    private String OrderDetailsID;
    private String OrderID;
    private String ProductID;
    private String Quantity;
}
