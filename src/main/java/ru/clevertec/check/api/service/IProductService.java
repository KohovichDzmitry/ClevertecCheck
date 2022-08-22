package ru.clevertec.check.api.service;

import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;

public interface IProductService extends GenericService<Product> {

    Product getProductByName(String name);

    CustomList<Product> getAllSortedByAlphabet();

    CustomList<Product> getAllSortedByPrice();
}
