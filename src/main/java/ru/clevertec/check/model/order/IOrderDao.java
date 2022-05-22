package ru.clevertec.check.model.order;

import ru.clevertec.check.util.CustomList;

public interface IOrderDao {

    void buy(int id, int quantity);

    CustomList<Order> getAll();
}
