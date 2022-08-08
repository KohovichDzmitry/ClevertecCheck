package ru.clevertec.check.model;

public class IdGenerator {

    private static int productId = 1;

    public static int generateProductId() {
        return productId++;
    }
}
