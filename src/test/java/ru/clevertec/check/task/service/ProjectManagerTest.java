//package ru.clevertec.check.task.service;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import ru.clevertec.check.api.dao.ICardDao;
//import ru.clevertec.check.api.dao.IProductDao;
//
//public class ProjectManagerTest {
//
//    static IProductDao productDao;
//    ICardDao cardDao;
//
//    @BeforeAll
//    static void generateProduct() {
//        productDao.getAll();
//    }
//
//    @BeforeEach
//    void generateCards() {
//        cardDao.getAll();
//    }
//
//    @AfterAll
//    static void deleteProductsAndOrder() {
//        productDao = null;
//    }
//
//    @AfterEach
//    void deleteCards() {
//        cardDao = null;
//    }
////
////    @DisplayName("Проверка наличия продукта из заказа по id - позитивный тест")
////    @Test
////    void listProductsFromOrderTest() {
////        CustomList<Product> actualList = projectService.listProductsFromOrder();
////        actualList.stream().forEach(product -> Assertions
////                .assertEquals(product, productDao.getById(product.getId())));
////    }
//
////    @DisplayName("Проверка наличия продукта из заказа по id - негативный тест")
////    @ParameterizedTest
////    @ValueSource(longs = {112, 22, 33, 45404})
////    void listProductsFromOrderFailTest(Long expectedProduct) {
////        Assertions.assertThrows(DaoException.class, () ->
////                productDao.getById(expectedProduct));
////    }
//
////    @DisplayName("Получение общей стоимости заказа - позитивный тест")
////    @Test
////    void getTotalSumTest() {
////        Double actualSum = projectService.getTotalSum();
////        Assertions.assertEquals(40.066, actualSum);
////    }
////
////    @DisplayName("Получение общей стоимости заказа - негативный тест")
////    @Test
////    void getTotalSumFailTest() {
////        Double actualSum = projectService.getTotalSum();
////        Assertions.assertNotEquals(401.066, actualSum);
////    }
//}
