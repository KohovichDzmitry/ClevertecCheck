package ru.clevertec.check.task.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.CardDao;
import ru.clevertec.check.dao.OrderDao;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.OrderService;

public class ProjectManagerTest {

    static IProductDao productDao = ProductDao.getInstance();
    static IOrderDao orderDao = OrderDao.getInstance();
    ICardDao cardDao = CardDao.getInstance();
    OrderService projectService = OrderService.getInstance();

    @BeforeAll
    static void generateProductsAndOrder() {
        productDao.getAll();
        orderDao.getAll();
    }

    @BeforeEach
    void generateCards() {
        cardDao.getAll();
    }

    @AfterAll
    static void deleteProductsAndOrder() {
        productDao = null;
        orderDao = null;
    }

    @AfterEach
    void deleteCards() {
        cardDao = null;
    }

    @DisplayName("Проверка наличия продукта из заказа по id - позитивный тест")
    @Test
    void listProductsFromOrderTest() {
        CustomList<Product> actualList = projectService.listProductsFromOrder();
        actualList.stream().forEach(product -> Assertions
                .assertEquals(product, productDao.getById(product.getId())));
    }

    @DisplayName("Проверка наличия продукта из заказа по id - негативный тест")
    @ParameterizedTest
    @ValueSource(longs = {112, 22, 33, 45404})
    void listProductsFromOrderFailTest(Long expectedProduct) {
        Assertions.assertThrows(DaoException.class, () ->
                productDao.getById(expectedProduct));
    }

    @DisplayName("Получение общей стоимости заказа - позитивный тест")
    @Test
    void getTotalSumTest() {
        Double actualSum = projectService.getTotalSum();
        Assertions.assertEquals(40.066, actualSum);
    }

    @DisplayName("Получение общей стоимости заказа - негативный тест")
    @Test
    void getTotalSumFailTest() {
        Double actualSum = projectService.getTotalSum();
        Assertions.assertNotEquals(401.066, actualSum);
    }
}
