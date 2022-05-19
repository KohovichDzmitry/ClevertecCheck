package ru.clevertec.check.model.product;

import ru.clevertec.check.model.checkRunner.CheckRunner;
import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomIterator;
import ru.clevertec.check.util.CustomList;

public class ProductDao implements IProductDao {

    private final CustomList<Product> listOfProducts = new CustomArrayList<>();

    @Override
    public void save(String name, double price, int stock) {
        Product product = new Product(name, price, stock);
        product.setId(IdGenerator.generateProductId());
        listOfProducts.add(product);
    }

    @Override
    public Product getById(int id) {
        CustomIterator<Product> productCustomIterator = listOfProducts.getIterator();
        while (productCustomIterator.hasNext()) {
            Product product = productCustomIterator.next();
            if (id == product.getId()) {
                return product;
            }
        }
        return null;
    }

    @Override
    public CustomList<Product> getAll() {
        return new CustomArrayList<>(listOfProducts);
    }
}
