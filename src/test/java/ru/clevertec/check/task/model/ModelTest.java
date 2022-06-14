package ru.clevertec.check.task.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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
                new Card(1111, 1),
                new Card(2222,2),
                new Card(3333, 3),
                new Card(4444, 4)
        );
    }

    @DisplayName("Получение скидочной карты по номеру - позитивный тест")
    @ParameterizedTest
    @MethodSource("getValidCards")
    void getCardByNumberTest(Card expectedCard) {
        Card actualCard = cardDao.getCardByNumber(expectedCard.getNumber());
        Assertions.assertEquals(expectedCard, actualCard);
    }

    @DisplayName("Получение скидочной карты по номеру - негативный тест")
    @ParameterizedTest
    @ValueSource(ints = {1112, 2122, 3332, 4544})
    void getCardByNumberFailTest(int expectedCard) {
        Assertions.assertThrows(ProjectException.class, () -> cardDao.getCardByNumber(expectedCard));
    }

    private static Stream<Order> getValidOrder() {
        return Stream.of(
                new Order(1, 1),
                new Order(2,1),
                new Order(3, 2),
                new Order(9, 5)
        );
    }

    @DisplayName("Получение продуктов из списка по id - позитивный тест")
    @ParameterizedTest
    @MethodSource("getValidOrder")
    void getProductFromOrderByIdTest(Order expectedOrder) {
        Order actualOrder = orderDao.getProductFromOrderById(expectedOrder.getId());
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    @DisplayName("Получение продуктов из списка по id - негативный тест")
    @ParameterizedTest
    @ValueSource(ints = {4, -1, 101, 0})
    void getProductFromOrderByIdFailTest(int expectedOrder) {
        Assertions.assertThrows(ProjectException.class, () -> orderDao.getProductFromOrderById(expectedOrder));
    }

    @DisplayName("Получение продукта по id - позитивный тест")
    @Test
    void getProductByIdTest(Product expectedProduct) {
        int id = expectedProduct.getId();
        Product actualProduct = productDao.getProductById(id);
        Assertions.assertEquals(expectedProduct, actualProduct);
    }

    @DisplayName("Получение продукта по id - негативный тест")
    @ParameterizedTest
    @ValueSource(ints = {44, -1, 101, 0})
    void getProductByIdFailTest(int expectedProduct) {
        Assertions.assertThrows(ProjectException.class, () -> productDao.getProductById(expectedProduct));
    }
}
