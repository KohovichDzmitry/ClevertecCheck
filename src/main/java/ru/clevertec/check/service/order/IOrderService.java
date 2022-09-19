package ru.clevertec.check.service.order;

import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;

import java.io.OutputStream;
import java.util.Map;

public interface IOrderService {

    void printCheck(Map<String, String[]> map, OutputStream out) throws ServiceException;

    void parseOrder(CustomList<Product> products, String[] arrayCount) throws ServiceException;

    Double getProductStockCost(CustomList<Product> products, Long id, Boolean mark) throws ServiceException;

    Long numberOfProductsFromOrderWithStock(CustomList<Product> customList);

    Double getTotalSum(CustomList<Product> products) throws ServiceException;
}
