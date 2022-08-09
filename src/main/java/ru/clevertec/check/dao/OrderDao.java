package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.dao.connection.ConnectionPool;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;

import java.sql.*;

public class OrderDao implements IOrderDao {

    private final CustomList<Order> listOfOrders = new CustomArrayList<>();
    private static final String INSERT_ORDER = "INSERT INTO custom_order (id_product, quantity) values (?, ?)";
    private static final String GET_ORDER_BY_ID = "SELECT products.id, products.name, products.price, " +
            "products.stock, custom_order.order_id, custom_order.quantity FROM custom_order JOIN products " +
            "ON products.id = custom_order.id_product WHERE custom_order.order_id = ?";
    private static final String GET_ALL_PRODUCTS = "SELECT id, name, price, stock FROM products";
    private static final String UPDATE_PRODUCT = "UPDATE products SET name = ?, price = ?, stock = ? WHERE id = ?";
    private static final String DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE id = ?";

    @Override
    public Order save(Order order) {
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getProduct().getId());
            statement.setInt(2, order.getQuantity());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                final int key = resultSet.getInt(1);
                order.setId(key);
            } else {
                throw new DaoException("Не удалось автоматически создать id для заказа");
            }
            return order;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при добавлении заказа в БД ", e);
        }
    }

    @Override
    public Order getOrderById(Integer id) {
        Order order;
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ORDER_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                order = createFromResultSet(resultSet);
            } else {
                throw new DaoException(String.format("Не удалось найти заказ по введённому id: %d", id));
            }
            return order;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке найти заказ по введённому id: %d", id));
        }
    }

    @Override
    public CustomList<Order> getOrder() {
        return null;
    }

    private Order createFromResultSet(ResultSet resultSet) {
        try {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setStock(resultSet.getInt("stock"));

            Order order = new Order();
            order.setId(resultSet.getInt("order_id"));
            order.setProduct(product);
            order.setQuantity(resultSet.getInt("quantity"));
            return order;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
