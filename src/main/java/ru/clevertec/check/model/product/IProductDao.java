package ru.clevertec.check.model.product;

public interface IProductDao {

    void save(String name, Double price, int stock);

    Product getById(Integer id);
}
