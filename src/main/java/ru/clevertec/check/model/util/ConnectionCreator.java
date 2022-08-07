package ru.clevertec.check.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionCreator {

    private static final String DATABASE_URL = DBPropertiesManager.getURL();
    private static final String DATABASE_USER = DBPropertiesManager.getUSER();
    private static final String DATABASE_PASSWORD = DBPropertiesManager.getPassword();

    static ProxyConnection createConnection() {
        Connection connection;
        ProxyConnection proxyConnection;
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            proxyConnection = new ProxyConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Не удается создать подключение к базе данных ", e);
        }
        return proxyConnection;
    }
}
