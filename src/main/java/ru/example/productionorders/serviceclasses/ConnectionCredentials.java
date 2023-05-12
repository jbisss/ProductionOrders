package ru.example.productionorders.serviceclasses;

import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Getter
public class ConnectionCredentials implements DisposableBean {

    private final String URL = "jdbc:postgresql://localhost:5432/OrderDb";
    private final String USER_NAME = "postgres";
    private final String PASSWORD = "root";
    private Connection connection = null;

    {
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
