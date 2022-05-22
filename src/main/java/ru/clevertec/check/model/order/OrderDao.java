package ru.clevertec.check.model.order;

import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomList;

public class OrderDao implements IOrderDao {

    private final CustomList<Order> listOfOrders = new CustomArrayList<>();

    @Override
    public void buy(int id, int quantity) {
        Order checkRunner = new Order(id, quantity);
        listOfOrders.add(checkRunner);
    }

    @Override
    public CustomList<Order> getAll() {
        return new CustomArrayList<>(listOfOrders);
    }
}
