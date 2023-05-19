package ru.example.productionorders.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shipper {

    private String ShipperID;
    private String ShipperName;
    private String Phone;

    @Override
    public String toString() {
        return "(" +
                ShipperID + ", " +
                "'" + ShipperName + "', " +
                "'" + Phone + "'" +
                ")";
    }
}
