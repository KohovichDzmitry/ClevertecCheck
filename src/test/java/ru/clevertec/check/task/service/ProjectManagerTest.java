package ru.clevertec.check.task.service;

import org.junit.jupiter.api.*;
import ru.clevertec.check.exception.ProjectException;
import ru.clevertec.check.io.CardReader;
import ru.clevertec.check.io.OrderReader;
import ru.clevertec.check.io.ProductReader;
import ru.clevertec.check.model.card.Card;
import ru.clevertec.check.model.card.CardDao;
import ru.clevertec.check.model.card.ICardDao;
import ru.clevertec.check.model.order.IOrderDao;
import ru.clevertec.check.model.order.Order;
import ru.clevertec.check.model.order.OrderDao;
import ru.clevertec.check.model.product.IProductDao;
import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.model.product.ProductDao;
import ru.clevertec.check.service.ProjectService;
import ru.clevertec.check.util.CustomList;

public class ProjectManagerTest {

    static CustomList<Product> productCustomList;
    static CustomList<Order> orderCustomList;
    CustomList<Card> cardCustomList;
    static IProductDao productDao = new ProductDao();
    static IOrderDao orderDao = new OrderDao();
    ICardDao cardDao = new CardDao();
    ProjectService projectService = new ProjectService(productDao, cardDao, orderDao);

    @BeforeAll
    static void generateProductList() {
        ProductReader productReader = new ProductReader();
        productReader.read(productDao);
        productCustomList = productDao.getProducts();
    }

    @BeforeAll
    static void generateOrderList() {
        OrderReader orderReader = new OrderReader();
        orderReader.read(orderDao);
        orderCustomList = orderDao.getOrder();
    }

    @BeforeEach
    void generateCardList() {
        CardReader cardReader = new CardReader();
        cardReader.read(cardDao);
        cardCustomList = cardDao.getCards();
    }

    @AfterAll
    static void deleteProductList() {
        productDao = null;
        productCustomList = null;
    }

    @AfterAll
    static void deleteOrderList() {
        orderDao = null;
        orderCustomList = null;
    }

    @AfterEach
    void deleteCardList() {
        cardDao = null;
        cardCustomList = null;
    }

    @DisplayName("Получение скидочной карты по номеру")
    @Test
    void getCardByNumberTest() {
        Card expectedCard = new Card(1111, 1);
        Card actualCard = cardDao.getCardByNumber(1111);
        Assertions.assertEquals(expectedCard, actualCard);
    }

    @Test
    void getCardByNumberFailTest() {
        Assertions.assertThrows(ProjectException.class, () -> cardDao.getCardByNumber(1112));
    }
}
