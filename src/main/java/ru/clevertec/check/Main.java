package ru.clevertec.check;

import ru.clevertec.check.io.*;
import ru.clevertec.check.model.card.CardDao;
import ru.clevertec.check.model.card.ICardDao;
import ru.clevertec.check.model.order.IOrderDao;
import ru.clevertec.check.model.order.OrderDao;
import ru.clevertec.check.model.product.IProductDao;
import ru.clevertec.check.model.product.ProductDao;
import ru.clevertec.check.service.ProjectService;

public class Main {

    private static final IProductDao productDao = new ProductDao();
    private static final ICardDao cardDao = new CardDao();
    private static final IOrderDao orderDao = new OrderDao();
    private static final ProjectService projectService = new ProjectService(productDao, cardDao, orderDao);

    public static void main(String[] args) {

        ProductReader productReader = new ProductReader();
        productReader.read(productDao);
        //создали и сохранили 20 продуктов из файла products; stock: 1 - товар на акции, 0 - не на акции

        CardReader cardReader = new CardReader();
        cardReader.read(cardDao);
        // создали и сохранили 5 скидочных карт из файла cards

        OrderReader orderReader = new OrderReader();
        orderReader.read(orderDao);
        //создали и сохранили содержимое чека (для примера) из файла order

        OutputCheck outputCheck = new OutputCheck(projectService);
        outputCheck.printCheck(4444);
        //печать чека в файл check, с учетом скидки по предьявленной карте
    }
}
