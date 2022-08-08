package ru.clevertec.check.api.dao;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.custom.CustomList;

public interface IOrderDao {

    void buyProduct(int id, int quantity);

    Order getProductFromOrderById(int id);

    CustomList<Order> getOrder();
}
