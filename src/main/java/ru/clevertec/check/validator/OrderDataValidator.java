package ru.clevertec.check.validator;

import java.util.Map;

public class OrderDataValidator {

    private static final String ORDER_PRODUCT_ID_REGEX = "([1-9]|[1-9]\\d|100)";
    private static final String ORDER_QUANTITY_REGEX = "([1-9]|1\\d|20)";

    public OrderDataValidator() {
    }

    public static boolean isValidOrderParameters(Map<String, String> orderParameters) {
        if (!isValidProductIDInOrder(orderParameters.get("id_product"))) {
            return false;
        }
        return isValidQuantityProductsInOrder(orderParameters.get("stock"));
    }

    public static boolean isValidProductIDInOrder(String id_product) {
        return id_product != null && !id_product.isEmpty() && id_product.matches(ORDER_PRODUCT_ID_REGEX);
    }

    public static boolean isValidQuantityProductsInOrder(String quantity) {
        return quantity != null && !quantity.isEmpty() && quantity.matches(ORDER_QUANTITY_REGEX);
    }

}
