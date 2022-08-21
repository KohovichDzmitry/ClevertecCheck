package ru.clevertec.check.validator;

import java.util.Map;

public class ProductDataValidator {

    private static final String PRODUCT_NAME_REGEX = "(([А-ЯЁ][а-яё]{2,29})|([A-Z][a-z]{2,29}))";
    private static final String PRODUCT_PRICE_REGEX = "((([1-9]|[1-9]\\d)\\.[0-9]{2})|(100\\.00))";
    private static final String PRODUCT_STOCK_REGEX = "([10])";

    public ProductDataValidator() {
    }

    public static boolean isValidProductParameters(Map<String, String> productParameters) {
        if (!isValidNameProduct(productParameters.get("product_name"))) {
            return false;
        }
        if (!isValidPriceProduct(productParameters.get("price"))) {
            return false;
        }
        return isValidStockProduct(productParameters.get("stock"));
    }

    public static boolean isValidNameProduct(String name) {
        return name != null && !name.isEmpty() && name.matches(PRODUCT_NAME_REGEX);
    }

    public static boolean isValidPriceProduct(String price) {
        return price != null && !price.isEmpty() && price.matches(PRODUCT_PRICE_REGEX);
    }

    public static boolean isValidStockProduct(String stock) {
        return stock != null && !stock.isEmpty() && stock.matches(PRODUCT_STOCK_REGEX);
    }
}
