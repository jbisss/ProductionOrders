open module ru.example.productionorders {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires lombok;
    requires org.slf4j;
    requires org.jetbrains.annotations;

    exports ru.example.productionorders.controllers;
    exports ru.example.productionorders;
    exports ru.example.productionorders.classes;
    exports ru.example.productionorders.repositories;
    exports ru.example.productionorders.serviceclasses;
}