package ru.example.productionorders.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shipper {

    private String ShipperID;
    private String ShipperName;
    private String Phone;

    public Shipper(){}

    public Shipper(String shipperID, String shipperName, String phone) {
        ShipperID = shipperID;
        ShipperName = shipperName;
        Phone = phone;
    }

    @Override
    public int hashCode(){
        return Integer.parseInt(ShipperID);
    }

    @Override
    public boolean equals(Object other){
        if(other == null || other.getClass() != this.getClass()) {
            return false;
        }
        return this.ShipperID.equals(((Shipper) other).getShipperID());
    }

    @Override
    public String toString() {
        return "(" +
                ShipperID + ", " +
                "'" + ShipperName + "', " +
                "'" + Phone + "'" +
                ")";
    }
}
