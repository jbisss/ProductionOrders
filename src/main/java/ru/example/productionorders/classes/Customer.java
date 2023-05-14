package ru.example.productionorders.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

    private String CustomerID;
    private String CustomerName;
    private String Address;
    private String City;
    private String Country;
    private String PostalCode;
}
