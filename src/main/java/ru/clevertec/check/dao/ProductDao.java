package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.ConnectionException;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.connection.ConnectionPool;
import ru.clevertec.check.model.Product;

import java.sql.*;

public class ProductDao implements IProductDao {

    private final CustomList<Product> listOfProducts = new CustomArrayList<>();
    private static final String INSERT_PRODUCT = "INSERT INTO products (name, price, stock) values (?, ?, ?)";
    private static final String GET_PRODUCT_BY_ID = "SELECT id, name, price, stock FROM products WHERE id = ?";

//    @Override
//    public void saveProduct(String name, double price, int stock) {
//        Product product = new Product(name, price, stock);
//        product.setId(IdGenerator.generateProductId());
//        listOfProducts.add(product);
//    }

    @Override
    public Product saveProduct(Product product) {
        try(Connection connection = ConnectionPool.INSTANCE.getConnection();
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
                throw new DaoException("Не удалось автоматически создать id");
            }
            return product;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при добавлении данных в БД ", e);
        }
    }

//    @Override
//    public Product getProductById(int id) {
//        return listOfProducts.stream()
//                .filter(product -> product.getId() == id)
//                .findFirst()
//                .orElseThrow(() -> new DaoException("Товара с выбранным id не существует"));
//    }

    @Override
    public Product getProductById(Integer id) {
        Product product = new Product();
        try(Connection connection = ConnectionPool.INSTANCE.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_PRODUCT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product = createFromResultSet(resultSet);
            }
            return product;
        } catch (SQLException e) {
            throw new DaoException(String.format("Не удалось найти объект по введённому id: %d", id));
        }
    }

    @Override
    public CustomList<Product> getAllProducts() {
        return new CustomArrayList<>(listOfProducts);
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
            throw new ConnectionException(e);
        }
    }
}
