package ru.example.productionorders.classes;

import lombok.Getter;
import ru.example.productionorders.dao.Customer;

import java.util.List;

@Getter
public class RandomOrder {

    private final Customer customer;
    private final List<RandomCategorie> categorieList;

    public RandomOrder(Customer customer, List<RandomCategorie> categorieList) {
        this.customer = customer;
        this.categorieList = categorieList;
    }

    @Override
    public String toString() {
        return this.customer + "{ " + categorieList + " }";
    }
}
