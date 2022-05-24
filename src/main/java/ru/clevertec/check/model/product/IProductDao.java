package ru.clevertec.check.model.product;

import ru.clevertec.check.util.CustomList;

public interface IProductDao {

    void saveProduct(String name, double price, int stock);

    Product getProductById(int id);

    CustomList<Product> getProducts();
}
