package ru.clevertec.check.model.checkRunner;

public class CheckRunner {

    private final Integer id;
    private final Integer quantity;

    public CheckRunner(Integer id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getId() {
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
