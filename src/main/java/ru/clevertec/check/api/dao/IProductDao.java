package ru.clevertec.check.api.dao;

import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;

public interface IProductDao {

    Product saveProduct(Product product);

    Product getProductById(Integer id);

    CustomList<Product> getAllProducts();

    Product updateProduct(Product product, Integer id);

    void deleteProduct(Integer id);
}
