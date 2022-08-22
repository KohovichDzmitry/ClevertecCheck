package ru.clevertec.check.api.service;

import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;

public interface IOrderService extends GenericService<Order> {

    Order getOrderByIdProduct(Long id);

    CustomList<Product> listProductsFromOrder();

    Long numberOfProductsFromOrderWithStock(CustomList<Product> customList);

    Double getTotalSum();

    Double getProductStockCost(Long id, Boolean mark);
}
