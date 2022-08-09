package ru.clevertec.check;

import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.dao.CardDao;
import ru.clevertec.check.dao.OrderDao;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.service.ProjectService;

public class Main {

    private static final IProductDao productDao = new ProductDao();
    private static final ICardDao cardDao = new CardDao();
    private static final IOrderDao orderDao = new OrderDao();
    private static final ProjectService projectService = new ProjectService(productDao, cardDao, orderDao);

    public static void main(String[] args) {

//        ProductReader productReader = new ProductReader();
//        productReader.read(productDao);
//        //создали и сохранили 20 продуктов из файла products; stock: 1 - товар на акции, 0 - не на акции
//
//        CardReader cardReader = new CardReader();
//        cardReader.read(cardDao);
//        // создали и сохранили 5 скидочных карт из файла cards
//
//        OrderReader orderReader = new OrderReader();
//        orderReader.read(orderDao);
//        //создали и сохранили содержимое чека (для примера) из файла order
//
//        OutputCheck outputCheck = new OutputCheck(projectService);
//        outputCheck.printCheck(4444);
//        //печать чека в файл check, с учетом скидки по предьявленной карте

//        Product product = new Product("Колбаса", 12.25, 1);
//        System.out.println(productDao.updateProduct(product, 21));
////        System.out.println(productDao.getProductById(21));
//        productDao.deleteProduct(21);

//        System.out.println(cardDao.getCardByNumber(111));
//        System.out.println(cardDao.getAllCards());

        System.out.println(orderDao.getOrderById(2));
    }
}
