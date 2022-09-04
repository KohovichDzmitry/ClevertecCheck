package ru.clevertec.check.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.dao.connection.ConnectionPool;
import ru.clevertec.check.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDao extends AbstractDao<Product> implements IProductDao {

    private static final ProductDao INSTANCE = new ProductDao();

    private static final String NAME = "продукт";
    private static final String INSERT_PRODUCT = "INSERT INTO products (product_name, price, stock) VALUES (?, ?, ?)";
    private static final String GET_PRODUCT_BY_ID = "SELECT product_id, product_name, price, stock " +
            "FROM products WHERE product_id = ?";
    private static final String GET_PRODUCT_BY_NAME = "SELECT product_id, product_name, price, stock " +
            "FROM products WHERE product_name = ?";
    private static final String GET_ALL_PRODUCTS = "SELECT product_id, product_name, price, stock FROM products";
    private static final String FIND_ALL_PRODUCTS = "SELECT product_id, product_name, price, stock FROM products " +
            "LIMIT ? OFFSET ?";
    private static final String UPDATE_PRODUCT_BY_ID = "UPDATE products SET product_name = ?, price = ?, stock = ? " +
            "WHERE product_id = ?";
    private static final String DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE product_id = ?";

    public static ProductDao getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getEntityName() {
        return NAME;
    }

    @Override
    protected String getInsertQuery() {
        return INSERT_PRODUCT;
    }

    @Override
    protected String getFindQuery() {
        return GET_PRODUCT_BY_ID;
    }

    @Override
    protected String getAllQuery() {
        return GET_ALL_PRODUCTS;
    }

    @Override
    protected String getFindAllQuery() {
        return FIND_ALL_PRODUCTS;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_PRODUCT_BY_ID;
    }

    @Override
    protected String getDeleteQuery() {
        return DELETE_PRODUCT_BY_ID;
    }

    @Override
    protected void prepareStatementForSave(PreparedStatement statement, Product product) throws DaoException {
        try {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос");
        }
    }

    @Override
    protected Product prepareStatementForFind(ResultSet resultSet) throws DaoException {
        try {
            Product product = new Product();
            product.setId(resultSet.getLong("product_id"));
            product.setName(resultSet.getString("product_name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setStock(resultSet.getInt("stock"));
            return product;
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос");
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Product product, Long id) throws DaoException {
        try {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
            statement.setLong(4, id);
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос");
        }
    }

    @Override
    public Product getProductByName(String name) throws DaoException {
        Product product;
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PRODUCT_BY_NAME)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product = prepareStatementForFind(resultSet);
            } else {
                throw new DaoException(String.format("Не удалось найти " + NAME + " по введённому названию: %s", name));
            }
            return product;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке найти " + NAME + " по введённому названию: %s", name));
        }
    }
}
