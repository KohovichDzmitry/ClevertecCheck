package ru.clevertec.check.model.product;

public interface IProductDao {

    void saveProduct(String name, double price, int stock);

    Product getProductById(int id);
}
