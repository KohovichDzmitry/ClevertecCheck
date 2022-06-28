package ru.clevertec.check.service;

import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.util.CustomList;

import java.io.PrintWriter;

public interface IProjectService {

    CustomList<Product> listProductsFromOrder();

    long numberOfProductsFromOrderWithStock(CustomList<Product> customList);

    double getTotalSum();

    double getProductStockCost(int id, boolean mark);

    void printProductFromTheOrder(PrintWriter pw);

    void printEndingCheck(PrintWriter pw, int cardNumber);
}
