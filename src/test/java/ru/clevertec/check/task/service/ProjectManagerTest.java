package ru.clevertec.check.task.service;

import org.junit.jupiter.api.*;
import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.CardDao;
import ru.clevertec.check.dao.OrderDao;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.ProjectService;

public class ProjectManagerTest {

    static IProductDao productDao = new ProductDao();
    static IOrderDao orderDao = new OrderDao();
    ICardDao cardDao = new CardDao();
    ProjectService projectService = new ProjectService(productDao, cardDao, orderDao);

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
    @Test
    void listProductsFromOrderFailTest() {
        CustomList<Product> actualList = projectService.listProductsFromOrder();
        actualList.add(new Product("Вафли", 2.21, 1));
        actualList.add(new Product("Майонез", 1.55, 0));
        actualList.add(new Product("Сыр", 3.48, 1));
        Assertions.assertThrows(DaoException.class, () -> actualList
                .stream().forEach(product -> Assertions
                        .assertEquals(product, productDao.getById(product.getId()))));
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
