package ru.clevertec.check.service;

import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.service.impl.CheckInfo;

import java.util.List;
import java.util.Map;

public interface IOrderService {

    CheckInfo readCheck(Map<String, String[]> map) throws ServiceException;

    void parseOrder(List<Product> products, String[] arrayCount) throws ServiceException;

    Double getProductStockCost(List<Product> products, Long id, Boolean mark) throws ServiceException;

    Long numberOfProductsFromOrderWithStock(List<Product> customList);

    Double getTotalSum(List<Product> products) throws ServiceException;
}
