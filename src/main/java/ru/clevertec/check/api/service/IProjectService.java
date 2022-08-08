package ru.clevertec.check.api.service;

import ru.clevertec.check.model.Product;
import ru.clevertec.check.custom.CustomList;

public interface IProjectService {

    CustomList<Product> listProductsFromOrder();

    long numberOfProductsFromOrderWithStock(CustomList<Product> customList);

    double getTotalSum();

    double getProductStockCost(int id, boolean mark);
}
