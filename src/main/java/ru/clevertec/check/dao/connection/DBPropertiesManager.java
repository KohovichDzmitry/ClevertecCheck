package ru.clevertec.check.dao.connection;

import ru.clevertec.check.api.exceptions.ConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DBPropertiesManager {

    private static final Properties properties;
    private static final String PROPERTIES_FILE = "database.properties";
    private static final String DRIVER = "database.driver";
    private static final String URL = "database.url";
    private static final String USER = "database.user";
    private static final String PASSWORD = "database.password";
    private static final String POOL_SIZE = "pool.size";

    static {
        properties = new Properties();
        try {
            InputStream inputStream = DBPropertiesManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (inputStream == null) {
                throw new ConnectionException("Файл database.properties не найден");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String getDriver() {
        return properties.getProperty(DRIVER);
    }

    static String getURL() {
        return properties.getProperty(URL);
    }

    static String getUSER() {
        return properties.getProperty(USER);
    }

    static String getPassword() {
        return properties.getProperty(PASSWORD);
    }

    static String getPoolSize() {
        return properties.getProperty(POOL_SIZE);
    }
}
