package ru.clevertec.check.model.order;

public class Order {

    private final int id;
    private final int quantity;

    public Order(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
