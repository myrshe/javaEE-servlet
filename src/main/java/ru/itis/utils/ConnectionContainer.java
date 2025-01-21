package ru.itis.utils;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionContainer {
    private static Connection connection;

    @SneakyThrows
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sem_work","postgres", "123");
        }
        return connection;
    }
}
