package ru.example.productionorders.classes;

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
}
