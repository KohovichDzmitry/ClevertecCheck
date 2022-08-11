package ru.clevertec.check.task.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.io.CardReader;
import ru.clevertec.check.io.OrderReader;
import ru.clevertec.check.io.ProductReader;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.dao.CardDao;
import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.dao.OrderDao;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.task.resolver.ProductParameterResolver;

import java.util.stream.Stream;

@ExtendWith(ProductParameterResolver.class)
public class ModelTest {

    public static IProductDao productDao = new ProductDao();
    static IOrderDao orderDao = new OrderDao();
    static ICardDao cardDao = new CardDao();

    @BeforeAll
    static void generateProducts() {
        ProductReader productReader = new ProductReader();
        productReader.read(productDao);
    }

    @BeforeAll
    static void generateOrder() {
        OrderReader orderReader = new OrderReader();
        orderReader.read(orderDao);
    }

    @BeforeAll
    static void generateCards() {
        CardReader cardReader = new CardReader();
        cardReader.read(cardDao);
    }

    @AfterAll
    static void deleteProducts() {
        productDao = null;
    }

    @AfterAll
    static void deleteOrder() {
        orderDao = null;
    }

    @AfterAll
    static void deleteCards() {
        cardDao = null;
    }

    private static Stream<Card> getValidCards() {
        return Stream.of(
                cardDao.getById(2L),
                cardDao.getById(3L),
                cardDao.getById(4L),
                cardDao.getById(5L)
        );
    }

    @DisplayName("Получение скидочной карты по id - позитивный тест")
    @ParameterizedTest
    @MethodSource("getValidCards")
    void getCardByNumberTest(Card expectedCard) {
        Card actualCard = cardDao.getById(expectedCard.getId());
        Assertions.assertEquals(expectedCard, actualCard);
    }

    @DisplayName("Получение скидочной карты по id - негативный тест")
    @ParameterizedTest
    @ValueSource(longs = {1112, 2122, 3332, 4544})
    void getCardByNumberFailTest(Long expectedCard) {
        Assertions.assertThrows(DaoException.class, () -> cardDao.getById(expectedCard));
    }

    private static Stream<Order> getValidOrder() {
        return Stream.of(
                orderDao.getById(1L),
                orderDao.getById(2L),
                orderDao.getById(3L),
                orderDao.getById(9L)
        );
    }

    @DisplayName("Получение продуктов из заказа по id - позитивный тест")
    @ParameterizedTest
    @MethodSource("getValidOrder")
    void getProductFromOrderByIdTest(Order expectedOrder) {
        Order actualOrder = orderDao.getById(expectedOrder.getId());
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    @DisplayName("Получение продуктов из заказа по id - негативный тест")
    @ParameterizedTest
    @ValueSource(longs = {42, -1, 101, 0})
    void getProductFromOrderByIdFailTest(Long expectedOrder) {
        Assertions.assertThrows(DaoException.class, () -> orderDao.getById(expectedOrder));
    }

    @DisplayName("Получение продукта по id - позитивный тест")
    @Test
    void getProductByIdTest(Product expectedProduct) {
        Long id = expectedProduct.getId();
        Product actualProduct = productDao.getById(id);
        Assertions.assertEquals(expectedProduct, actualProduct);
    }

    @DisplayName("Получение продукта по id - негативный тест")
    @ParameterizedTest
    @ValueSource(longs = {44, -1, 101, 0})
    void getProductByIdFailTest(Long expectedProduct) {
        Assertions.assertThrows(DaoException.class, () -> productDao.getById(expectedProduct));
    }
}
