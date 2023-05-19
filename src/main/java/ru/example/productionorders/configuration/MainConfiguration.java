package ru.example.productionorders.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.example.productionorders.classes.Categorie;
import ru.example.productionorders.classes.Product;
import ru.example.productionorders.classes.Shipper;

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
}
