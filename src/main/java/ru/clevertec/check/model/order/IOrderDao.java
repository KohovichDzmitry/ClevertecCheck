package ru.clevertec.check.model.order;

import ru.clevertec.check.util.CustomList;

public interface IOrderDao {

    void buyOrder(int id, int quantity);

    Order getOrderById(int id);

    CustomList<Order> getOrder();
}
