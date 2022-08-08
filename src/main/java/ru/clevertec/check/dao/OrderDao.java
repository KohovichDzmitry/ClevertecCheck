package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;

public class OrderDao implements IOrderDao {

    private final CustomList<Order> listOfOrders = new CustomArrayList<>();

    @Override
    public void buyProduct(int id, int quantity) {
        Order checkRunner = new Order(id, quantity);
        listOfOrders.add(checkRunner);
    }

    @Override
    public Order getProductFromOrderById(int id) {
        return listOfOrders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElseThrow(() -> new DaoException("В заказе товара с выбранным id не существует"));
    }

    @Override
    public CustomList<Order> getOrder() {
        return new CustomArrayList<>(listOfOrders);
    }
}
