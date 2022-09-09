package ru.clevertec.check.dao.connection;

import ru.clevertec.check.api.exceptions.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public enum ConnectionPool {

    INSTANCE;

    private final BlockingQueue<ProxyConnection> freeConnections;
    private final BlockingQueue<ProxyConnection> activeConnections;

    private final int DEFAULT_POOL_SIZE = Integer.parseInt(DBPropertiesManager.getPoolSize());
    private final String DATABASE_DRIVER = DBPropertiesManager.getDriver();

    ConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(DEFAULT_POOL_SIZE);
        activeConnections = new LinkedBlockingQueue<>(DEFAULT_POOL_SIZE);
        registerDriver();
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            freeConnections.offer(ConnectionCreator.createConnection());
        }
    }

    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            activeConnections.put(connection);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection instanceof ProxyConnection && activeConnections.remove(connection)) {
            try {
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                throw new ConnectionException(e);
            }
            freeConnections.offer((ProxyConnection) connection);
        } else {
            System.out.println("Данный connection не принадлежит к ProxyConnection");
        }
    }

    public void destroyPool() {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            try {
                freeConnections.take().reallyClose();
            } catch (SQLException e) {
                throw new ConnectionException("Ошибка при закрытии потока", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void registerDriver() {
        try {
            Class.forName(DATABASE_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ConnectionException("Ошибка подключения к базе данных ", e);
        }
    }

    private void deregisterDriver() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.registerDriver(driver);
            } catch (SQLException e) {
                throw new ConnectionException("Ошибка при регистрации драйвера базы данных", e);
            }
        });
    }
}
