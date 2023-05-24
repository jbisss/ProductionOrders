package ru.example.productionorders.serviceclasses;

import org.springframework.stereotype.Service;
import ru.example.productionorders.classes.RandomCategorie;
import ru.example.productionorders.classes.RandomOrder;
import ru.example.productionorders.dao.Categorie;
import ru.example.productionorders.dao.Customer;
import ru.example.productionorders.dao.Product;
import ru.example.productionorders.repositories.OrderRandomizerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderRandomizer {

    private final OrderRandomizerRepository randomizerRepository;
    private final Cache cache;

    public OrderRandomizer(OrderRandomizerRepository randomizerRepository, Cache cache) {
        this.randomizerRepository = randomizerRepository;
        this.cache = cache;
    }

    public RandomOrder generateOrder() {
        // Choosing random customer
        List<Customer> customerList = randomizerRepository.getAllCustomers();
        int randomCustomer = new Random().nextInt(customerList.size());
        Customer customer = customerList.get(randomCustomer);

        // Choosing random categories
        List<Categorie> categorieList = cache.getCategorieMap().values().stream().toList();
        int randomCategoriesNumber = new Random().nextInt(4) + 1;
        List<RandomCategorie> categories = new ArrayList<>();
        for (int i = 0; i < randomCategoriesNumber; i++) {
            int randomCategorie = new Random().nextInt(categorieList.size());
            RandomCategorie categorieToAdd = new RandomCategorie(categorieList.get(randomCategorie));
            if (!categories.contains(categorieToAdd)) {
                categories.add(new RandomCategorie(categorieList.get(randomCategorie)));
            }
        }

        // Choosing random products for each category
        for (RandomCategorie categorie : categories) {
            int id = Integer.parseInt(categorie.getCategorie().getCategoryID());
            List<Product> productList = randomizerRepository.getProductsByCategorie(id);
            if (productList.size() != 0) {
                int randomProductCount = new Random().nextInt(5) + 1;
                for (int i = 0; i < randomProductCount; i++) {
                    int randomProduct = new Random().nextInt(productList.size());
                    if (!categorie.getProductList().contains(productList.get(randomProduct))) {
                        categorie.getProductList().add(productList.get(randomProduct));
                    }
                }
            }
        }
        if (categories.size() == 0) {
            return null;
        }
        return new RandomOrder(customer, categories);
    }
}
