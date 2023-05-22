package ru.example.productionorders.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Categorie {

    private String CategoryID;
    private String CategoryName;
    private String Description;

    public Categorie(){}

    public Categorie(String categoryID, String categoryName, String description) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        Description = description;
    }

    @Override
    public String toString() {
        return "(" +
                CategoryID + ", " +
                "'" + CategoryName + "', " +
                "'" + Description + "'" +
                ")";
    }
}
