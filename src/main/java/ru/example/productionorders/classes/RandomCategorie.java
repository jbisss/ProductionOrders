package ru.example.productionorders.classes;

import lombok.Getter;
import ru.example.productionorders.dao.Categorie;
import ru.example.productionorders.dao.Product;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RandomCategorie {

    private final Categorie categorie;
    private final List<Product> productList = new ArrayList<>();

    public RandomCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null || other.getClass() != this.getClass()) {
            return false;
        }
        return this.categorie.getCategoryID().equals(((RandomCategorie) other).getCategorie().getCategoryID());
    }

    @Override
    public String toString() {
        return this.categorie + "[ " + productList + " ]";
    }
}
