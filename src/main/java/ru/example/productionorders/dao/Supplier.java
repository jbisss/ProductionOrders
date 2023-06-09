package ru.example.productionorders.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Supplier {

    private String SupplierID;
    private String SupplierName;
    private String ContactName;
    private String Address;
    private String City;
    private String Country;
    private String Phone;
    private String PostalCode;

    public Supplier(){}

    public Supplier(String supplierID, String supplierName, String contactName, String address, String city, String country, String phone, String postalCode) {
        SupplierID = supplierID;
        SupplierName = supplierName;
        ContactName = contactName;
        Address = address;
        City = city;
        Country = country;
        Phone = phone;
        PostalCode = postalCode;
    }

    @Override
    public String toString() {
        return "(" +
                SupplierID + ", " +
                "'" + SupplierName + "', " +
                "'" + ContactName + "', " +
                "'" + Address + "', " +
                "'" + City + "', " +
                "'" + Country + "', " +
                "'" + Phone + "', " +
                "'" + PostalCode + "'" +
                ")";
    }
}
