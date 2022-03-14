package ru.clevertec.check.model.checkRunner;

public class CheckRunner {

    private final int id;
    private final int quantity;

    public CheckRunner(int id, int quantity) {
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
        return "CheckRunner{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
