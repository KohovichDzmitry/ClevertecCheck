package ru.clevertec.check;

import ru.clevertec.check.io.OutputCheck;
import ru.clevertec.check.service.OrderService;

public class Main {

    //private static final IProductDao productDao = new ProductDao();
    //private static final IProductService productService = new ProductService(productDao);
    //private static final ICardDao cardDao = new CardDao();
    //private static final ICardService cardService = new CardService(cardDao);
    //private static final IOrderDao orderDao = new OrderDao();
    private static final OrderService orderService = OrderService.getInstance();

    public static void main(String[] args) {
        OutputCheck outputCheck = new OutputCheck(orderService);
        outputCheck.printCheck(5L);
    }
}
