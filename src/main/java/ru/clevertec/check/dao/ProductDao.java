package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.connection.ConnectionPool;
import ru.clevertec.check.model.Product;

import java.sql.*;

public class ProductDao implements IProductDao {

    private static final String INSERT_PRODUCT = "INSERT INTO products (name, price, stock) values (?, ?, ?)";
    private static final String GET_PRODUCT_BY_ID = "SELECT id, name, price, stock FROM products WHERE id = ?";
    private static final String GET_ALL_PRODUCTS = "SELECT id, name, price, stock FROM products";
    private static final String UPDATE_PRODUCT = "UPDATE products SET name = ?, price = ?, stock = ? WHERE id = ?";
    private static final String DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE id = ?";

    @Override
    public Product saveProduct(Product product) {
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                final int key = resultSet.getInt(1);
                product.setId(key);
            } else {
                throw new DaoException("Не удалось автоматически создать id для продукта");
            }
            return product;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при добавлении продукта в БД ", e);
        }
    }

    @Override
    public Product getProductById(Integer id) {
        Product product;
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PRODUCT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product = createFromResultSet(resultSet);
            } else {
                throw new DaoException(String.format("Не удалось найти продукт по введённому id: %d", id));
            }
            return product;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке найти продукт по введённому id: %d", id));
        }
    }

    @Override
    public CustomList<Product> getAllProducts() {
        CustomList<Product> products = new CustomArrayList<>();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_PRODUCTS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = createFromResultSet(resultSet);
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при попытке найти продукты в БД", e);
        }
    }

    @Override
    public Product updateProduct(Product product, Integer id) {
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
            statement.setInt(4, id);
            product.setId(id);
            if (statement.executeUpdate() != 1) {
                throw new DaoException(String.format("Не удалось найти продукт по введённому id: %d", id));
            }
            return product;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке обновить продукт по введённому id: %d", id));
        }
    }

    @Override
    public void deleteProduct(Integer id) {
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_BY_ID)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() != 1) {
                throw new DaoException(String.format("Не удалось найти продукт по введённому id: %d", id));
            }
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке удалить продукт по введённому id: %d", id));
        }
    }

    private Product createFromResultSet(ResultSet resultSet) {
        try {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setStock(resultSet.getInt("stock"));
            return product;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
