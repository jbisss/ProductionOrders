package ru.example.productionorders.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

    private String OrderID;
    private String CustomerID;
    private String EmployeeID;
    private String OrderDate;
    private String ShipperID;
}
