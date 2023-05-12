package ru.example.productionorders.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ru.example.productionorders.classes", "ru.example.productionorders.serviceclasses"})
public class MainConfiguration {

}
