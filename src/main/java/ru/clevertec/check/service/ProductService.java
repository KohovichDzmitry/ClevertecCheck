package ru.clevertec.check.service;

import ru.clevertec.check.model.order.IOrderDao;
import ru.clevertec.check.model.order.Order;
import ru.clevertec.check.model.product.IProductDao;
import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.util.CustomList;

import java.io.PrintWriter;
import java.util.Arrays;

public class ProductService {

    private final IOrderDao orderDao;
    private final IProductDao productDao;

    public ProductService(IOrderDao orderDao, IProductDao productDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    public CustomList<Product> listProductsFromOrder() {
        return CustomList.toCustomList(Arrays
                .stream(orderDao.getAll().stream()
                        .mapToInt(Order::getId).toArray())
                .boxed().map(productDao::getById).toArray());
    }

    public long numberOfProductsFromOrderWithStock(CustomList<Product> customList) {
        return customList.stream()
                .filter(e -> e.getStock() == 1).count();
    }

    public void printProductFromTheOrder(PrintWriter pw) {
        CustomList<Product> actualList = listProductsFromOrder();
        long number = numberOfProductsFromOrderWithStock(actualList);


    }


}
