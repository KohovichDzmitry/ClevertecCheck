package ru.clevertec.check.api.service;

import ru.clevertec.check.model.Product;
import ru.clevertec.check.custom.CustomList;

public interface IProjectService {

    CustomList<Product> listProductsFromOrder();

    Long numberOfProductsFromOrderWithStock(CustomList<Product> customList);

    Double getTotalSum();

    Double getProductStockCost(Long id, Boolean mark);
}
