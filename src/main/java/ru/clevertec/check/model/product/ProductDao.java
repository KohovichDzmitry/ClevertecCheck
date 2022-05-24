package ru.clevertec.check.model.product;

import ru.clevertec.check.exception.ProjectException;
import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomList;

public class ProductDao implements IProductDao {

    private final CustomList<Product> listOfProducts = new CustomArrayList<>();

    @Override
    public void saveProduct(String name, double price, int stock) {
        Product product = new Product(name, price, stock);
        product.setId(IdGenerator.generateProductId());
        listOfProducts.add(product);
    }

    @Override
    public Product getProductById(int id) {
        return listOfProducts.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProjectException("Товара с выбранным id не существует"));
    }

    @Override
    public CustomList<Product> getProducts() {
        return new CustomArrayList<>(listOfProducts);
    }
}
