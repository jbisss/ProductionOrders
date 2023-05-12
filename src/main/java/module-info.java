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

    exports ru.example.productionorders.controllers;
    exports ru.example.productionorders;
    exports ru.example.productionorders.classes;
}