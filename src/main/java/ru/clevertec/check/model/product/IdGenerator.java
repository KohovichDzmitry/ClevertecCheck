package ru.clevertec.check.model.product;

public class IdGenerator {

    private static Integer productId = 1;

    public static Integer generateProductId() {
        return productId++;
    }
}
