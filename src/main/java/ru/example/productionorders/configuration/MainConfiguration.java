package ru.example.productionorders.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.example.productionorders.classes.Product;

@Configuration
@ComponentScan(basePackages = {"ru.example.productionorders.classes", "ru.example.productionorders.serviceclasses"})
public class MainConfiguration {

    @Bean
    public Product transferProduct(){
        return new Product();
    }
}
