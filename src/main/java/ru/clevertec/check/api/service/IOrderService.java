package ru.clevertec.check.api.service;

import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;

import java.io.PrintWriter;

public interface IOrderService extends GenericService<Order> {

    Order getOrderByIdProduct(Long id) throws ServiceException;

    CustomList<Product> listProductsFromOrder() throws ServiceException;

    Long numberOfProductsFromOrderWithStock(CustomList<Product> customList);

    Double getTotalSum() throws ServiceException;

    Double getProductStockCost(Long id, Boolean mark) throws ServiceException;

    void printEndingCheck(PrintWriter pw, Long card_id) throws ServiceException;

    void printProductFromTheOrder(PrintWriter pw) throws ServiceException;
}
