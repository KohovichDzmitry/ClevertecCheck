package ru.clevertec.check.api.dao;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.custom.CustomList;

public interface IOrderDao {

    Order save(Order order);

    Order getOrderById(Integer id);

    CustomList<Order> getOrder();
}
