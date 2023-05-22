package ru.example.productionorders.dao;

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

    public Customer(){}

    public Customer(String customerID, String customerName, String address, String city, String country, String postalCode) {
        CustomerID = customerID;
        CustomerName = customerName;
        Address = address;
        City = city;
        Country = country;
        PostalCode = postalCode;
    }
}
