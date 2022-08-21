package ru.clevertec.check;

import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.dao.CardDao;
import ru.clevertec.check.dao.OrderDao;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.io.OutputCheck;
import ru.clevertec.check.service.OrderService;

public class Main {

    private static final IProductDao productDao = new ProductDao();
    //private static final IProductService productService = new ProductService(productDao);
    private static final ICardDao cardDao = new CardDao();
    //private static final ICardService cardService = new CardService(cardDao);
    private static final IOrderDao orderDao = new OrderDao();
    private static final OrderService orderService = new OrderService(productDao, cardDao, orderDao);

    public static void main(String[] args) {
        OutputCheck outputCheck = new OutputCheck(orderService);
        outputCheck.printCheck(5L);
    }
}
