package ru.clevertec.check.api.dao;

import ru.clevertec.check.model.Product;
import ru.clevertec.check.custom.CustomList;

public interface IProductDao {

    //void saveProduct(String name, double price, int stock);
    Product saveProduct(Product product);

    Product getProductById(Integer id);

    CustomList<Product> getAllProducts();
}
