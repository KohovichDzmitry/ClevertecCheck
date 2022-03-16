package ru.clevertec.check.model.product;

public interface IProductDao {

    void save(String name, double price, int stock);

    Product getById(int id);
}
