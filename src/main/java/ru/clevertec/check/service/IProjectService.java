package ru.clevertec.check.service;

import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.util.CustomList;

public interface IProjectService {

    CustomList<Product> listProductsFromOrder();

    long numberOfProductsFromOrderWithStock(CustomList<Product> customList);

    double getTotalSum();

    double getProductStockCost(int id, boolean mark);
}
