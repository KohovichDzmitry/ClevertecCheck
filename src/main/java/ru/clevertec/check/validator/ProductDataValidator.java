package ru.clevertec.check.validator;

import lombok.NoArgsConstructor;
import ru.clevertec.check.entity.Product;

@NoArgsConstructor
public class ProductDataValidator {

    private static final String PRODUCT_NAME_REGEX = "(([А-ЯЁ][а-яё]{2,29})|([A-Z][a-z]{2,29}))";
    private static final String PRODUCT_PRICE_REGEX = "((([1-9]|[1-9]\\d)\\.[0-9]{2})|(100\\.00))";
    private static final String PRODUCT_STOCK_REGEX = "([10])";
    private static final String PRODUCT_QUANTITY_REGEX = "([1-9]|1\\d|20)";

    public static boolean isValidProductParameters(Product product) {
        if (!isValidNameProduct(product.getName())) {
            return false;
        }
        if (!isValidPriceProduct(String.valueOf(product.getPrice()))) {
            return false;
        }
        return isValidStockProduct(String.valueOf(product.getStock()));
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

    public static boolean isValidQuantityProduct(String quantity) {
        return quantity != null && !quantity.isEmpty() && quantity.matches(PRODUCT_QUANTITY_REGEX);
    }
}