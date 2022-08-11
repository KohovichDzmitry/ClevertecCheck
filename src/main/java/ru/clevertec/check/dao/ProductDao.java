package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDao extends AbstractDao<Product> implements IProductDao {

    private static final String INSERT_PRODUCT = "INSERT INTO products (product_name, price, stock) values (?, ?, ?)";
    private static final String GET_PRODUCT_BY_ID = "SELECT product_id, product_name, price, stock " +
            "FROM products WHERE product_id = ?";
    private static final String GET_ALL_PRODUCTS = "SELECT product_id, product_name, price, stock FROM products";
    private static final String UPDATE_PRODUCT_BY_ID = "UPDATE products SET product_name = ?, price = ?, stock = ? " +
            "WHERE product_id = ?";
    private static final String DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE product_id = ?";

    @Override
    protected String getInsertQuery() {
        return INSERT_PRODUCT;
    }

    @Override
    protected String getFindQuery() {
        return GET_PRODUCT_BY_ID;
    }

    @Override
    protected String getFindAllQuery() {
        return GET_ALL_PRODUCTS;
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
    protected void prepareStatementForSave(PreparedStatement statement, Product product) {
        try {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос ", e);
        }
    }

    @Override
    protected Product prepareStatementForFind(ResultSet resultSet) {
        try {
            Product product = new Product();
            product.setId(resultSet.getLong("product_id"));
            product.setName(resultSet.getString("product_name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setStock(resultSet.getInt("stock"));
            return product;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Product product, Long id) {
        try {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
            statement.setLong(4, id);
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос ", e);
        }
    }
}
