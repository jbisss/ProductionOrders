package ru.example.productionorders.classes;

import lombok.Getter;

@Getter
public class Customer {

    private final Integer id;
    private final String name;
    private final String address;
    private final String city;
    private final String country;
    private final String postalCode;

    public Customer(int id, String name, String address, String city, String country, String postalCode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }
}
