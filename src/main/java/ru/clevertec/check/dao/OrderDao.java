package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDao extends AbstractDao<Order> implements IOrderDao {

    private static final String INSERT_ORDER = "INSERT INTO custom_order (id_product, quantity) values (?, ?)";
    private static final String GET_ORDER_BY_ID = "SELECT products.product_id, products.product_name, products.price, " +
            "products.stock, custom_order.order_id, custom_order.quantity FROM custom_order JOIN products " +
            "ON products.product_id = custom_order.id_product WHERE custom_order.id_product = ?";
    private static final String GET_ALL_ORDERS = "SELECT products.product_id, products.product_name, products.price, " +
            "products.stock, custom_order.order_id, custom_order.quantity FROM custom_order JOIN products " +
            "ON products.product_id = custom_order.id_product";
    private static final String UPDATE_ORDER_BY_ID = "UPDATE custom_order SET id_product = ?, quantity = ? WHERE order_id = ?";
    private static final String DELETE_ORDER_BY_ID = "DELETE FROM custom_order WHERE order_id = ?";

    @Override
    protected String getInsertQuery() {
        return INSERT_ORDER;
    }

    @Override
    protected String getFindQuery() {
        return GET_ORDER_BY_ID;
    }

    @Override
    protected String getFindAllQuery() {
        return GET_ALL_ORDERS;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_ORDER_BY_ID;
    }

    @Override
    protected String getDeleteQuery() {
        return DELETE_ORDER_BY_ID;
    }

    @Override
    protected void prepareStatementForSave(PreparedStatement statement, Order order) {
        try {
            statement.setLong(1, order.getProduct().getId());
            statement.setInt(2, order.getQuantity());
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос ", e);
        }
    }

    protected Order prepareStatementForFind(ResultSet resultSet) {
        try {
            Product product = new Product();
            product.setId(resultSet.getLong("product_id"));
            product.setName(resultSet.getString("product_name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setStock(resultSet.getInt("stock"));

            Order order = new Order();
            order.setId(resultSet.getLong("order_id"));
            order.setProduct(product);
            order.setQuantity(resultSet.getInt("quantity"));
            return order;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Order order, Long id) {
        try {
            statement.setLong(1, order.getProduct().getId());
            statement.setInt(2, order.getQuantity());
            statement.setLong(3, id);
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос ", e);
        }
    }

//    @Override
//    public Order saveOrder(Order order) {
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
//            statement.setLong(1, order.getProduct().getId());
//            statement.setInt(2, order.getQuantity());
//            statement.executeUpdate();
//            ResultSet resultSet = statement.getGeneratedKeys();
//            if (resultSet.next()) {
//                final Long key = resultSet.getLong(1);
//                order.setId(key);
//            } else {
//                throw new DaoException("Не удалось автоматически создать id для заказа");
//            }
//            return order;
//        } catch (SQLException e) {
//            throw new DaoException("Ошибка при добавлении заказа в БД ", e);
//        }
//    }
//
//    @Override
//    public Order getOrderById(Long id) {
//        Order order;
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(GET_ORDER_BY_ID)) {
//            statement.setLong(1, id);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                order = createFromResultSet(resultSet);
//            } else {
//                throw new DaoException(String.format("Не удалось найти заказ по введённому id: %d", id));
//            }
//            return order;
//        } catch (SQLException e) {
//            throw new DaoException(String.format("Ошибка при попытке найти заказ по введённому id: %d", id));
//        }
//    }
//
//    @Override
//    public CustomList<Order> getAllOrders() {
//        CustomList<Order> orders = new CustomArrayList<>();
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(GET_ALL_ORDERS)) {
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Order order = createFromResultSet(resultSet);
//                orders.add(order);
//            }
//            return orders;
//        } catch (SQLException e) {
//            throw new DaoException("Ошибка при попытке найти заказы в БД", e);
//        }
//    }
//
//    @Override
//    public Order updateOrder(Order order, Long id) {
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_BY_ID)) {
//            statement.setLong(1, order.getProduct().getId());
//            statement.setInt(2, order.getQuantity());
//            statement.setLong(3, id);
//            order.setId(id);
//            if (statement.executeUpdate() != 1) {
//                throw new DaoException(String.format("Не удалось найти заказ по введённому id: %d", id));
//            }
//            return order;
//        } catch (SQLException e) {
//            throw new DaoException(String.format("Ошибка при попытке обновить заказ по введённому id: %d", id));
//        }
//    }
//
//    @Override
//    public void deleteOrder(Long id) {
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_BY_ID)) {
//            statement.setLong(1, id);
//            if (statement.executeUpdate() != 1) {
//                throw new DaoException(String.format("Не удалось найти заказ по введённому id: %d", id));
//            }
//        } catch (SQLException e) {
//            throw new DaoException(String.format("Ошибка при попытке удалить заказ по введённому id: %d", id));
//        }
//    }
//
//    private Order createFromResultSet(ResultSet resultSet) {
//        try {
//            Product product = new Product();
//            product.setId(resultSet.getLong("product_id"));
//            product.setName(resultSet.getString("product_name"));
//            product.setPrice(resultSet.getDouble("price"));
//            product.setStock(resultSet.getInt("stock"));
//
//            Order order = new Order();
//            order.setId(resultSet.getLong("order_id"));
//            order.setProduct(product);
//            order.setQuantity(resultSet.getInt("quantity"));
//            return order;
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
//    }
}
