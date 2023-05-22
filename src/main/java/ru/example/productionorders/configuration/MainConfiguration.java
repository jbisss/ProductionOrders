package ru.example.productionorders.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.example.productionorders.dao.Categorie;
import ru.example.productionorders.dao.Product;
import ru.example.productionorders.dao.Shipper;
import ru.example.productionorders.dao.Supplier;

@Configuration
@ComponentScan(basePackages = {"ru.example.productionorders"})
public class MainConfiguration {

    @Bean
    public Product transferProduct(){
        return new Product();
    }

    @Bean
    public Shipper transferShipper() {
        return new Shipper();
    }

    @Bean
    public Categorie transferCategory(){
        return new Categorie();
    }

    @Bean
    public Supplier transferSupplier() {
        return new Supplier();
    }
}
