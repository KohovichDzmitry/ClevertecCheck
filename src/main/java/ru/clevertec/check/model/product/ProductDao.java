package ru.clevertec.check.model.product;

import java.util.ArrayList;
import java.util.List;

public class ProductDao implements IProductDao {

    private final List<Product> listOfProducts = new ArrayList<>();

    @Override
    public void save(String name, double price, int stock) {
        Product product = new Product(name, price, stock);
        product.setId(IdGenerator.generateProductId());
        listOfProducts.add(product);
    }

    @Override
    public Product getById(int id) {
        for (Product product : listOfProducts) {
            if (id == product.getId()) {
                return product;
            }
        }
        return null;
    }
}
