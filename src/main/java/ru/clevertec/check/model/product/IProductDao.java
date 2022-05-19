package ru.clevertec.check.model.product;

import ru.clevertec.check.util.CustomList;

public interface IProductDao {

    void save(String name, double price, int stock);

    Product getById(int id);

    CustomList<Product> getAll();
}
