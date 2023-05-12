package ru.example.productionorders.serviceclasses;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConnectionCredentials {

    private final String URL = "jdbc:postgresql://localhost:5432/OrderDb";
    private final String USER_NAME = "postgres";
    private final String PASSWORD = "root";
}
