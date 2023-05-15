package ru.example.productionorders.classes;

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
}
