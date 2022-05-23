package ru.clevertec.check.model.order;

import ru.clevertec.check.exception.ProjectException;
import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomList;

public class OrderDao implements IOrderDao {

    private final CustomList<Order> listOfOrders = new CustomArrayList<>();

    @Override
    public void buyOrder(int id, int quantity) {
        Order checkRunner = new Order(id, quantity);
        listOfOrders.add(checkRunner);
    }

    @Override
    public Order getOrderById(int id) {
        return listOfOrders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProjectException("В заказе товара с выбранным id не существует"));
    }

    @Override
    public CustomList<Order> getOrder() {
        return new CustomArrayList<>(listOfOrders);
    }
}
