package ru.example.productionorders.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("currentEmployee")
public class Employee {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String notes;
}
