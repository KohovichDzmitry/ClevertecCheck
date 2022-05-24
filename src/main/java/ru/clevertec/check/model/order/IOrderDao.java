package ru.clevertec.check.model.order;

import ru.clevertec.check.util.CustomList;

public interface IOrderDao {

    void buyProduct(int id, int quantity);

    Order getProductFromOrderById(int id);

    CustomList<Order> getOrder();
}
