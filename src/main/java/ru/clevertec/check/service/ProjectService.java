package ru.clevertec.check.service;

import ru.clevertec.check.exception.ProjectException;
import ru.clevertec.check.model.card.ICardDao;
import ru.clevertec.check.model.order.IOrderDao;
import ru.clevertec.check.model.order.Order;
import ru.clevertec.check.model.product.IProductDao;
import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.util.CustomList;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;

public class ProjectService implements IProjectService {

    private final IProductDao productDao;
    private final ICardDao cardDao;
    private final IOrderDao orderDao;
    private static final Double DISCOUNT_PERCENT = 10d;

    public ProjectService(IProductDao productDao, ICardDao cardDao, IOrderDao orderDao) {
        this.productDao = productDao;
        this.cardDao = cardDao;
        this.orderDao = orderDao;
    }

    @Override
    public CustomList<Product> listProductsFromOrder() {
        return CustomList.toCustomList(Arrays
                .stream(orderDao.getOrder().stream()
                        .mapToInt(Order::getId).toArray())
                .boxed().map(productDao::getProductById).toArray());
    }

    @Override
    public long numberOfProductsFromOrderWithStock(CustomList<Product> customList) {
        return customList.stream()
                .filter(number -> number.getStock() == 1).count();
    }

    @Override
    public double getTotalSum() {
        return orderDao.getOrder().stream()
                .map(product -> product.getQuantity() * getProductStockCost(product.getId(), true))
                .reduce(Double::sum)
                .orElseThrow(() -> new ProjectException("Неверный расчет стоимости заказа"));
    }

    @Override
    public double getProductStockCost(int id, boolean mark) {
        CustomList<Product> actualList = listProductsFromOrder();
        return Optional.of(productDao.getProductById(id))
                .filter(product -> mark && product.getStock() == 1
                        && numberOfProductsFromOrderWithStock(actualList) > 4)
                .map(product -> product.getPrice() * ((100 - DISCOUNT_PERCENT) / 100))
                .orElse(productDao.getProductById(id).getPrice());
    }

    @Override
    public void printProductFromTheOrder(PrintWriter pw) {
        CustomList<Product> actualList = listProductsFromOrder();
        actualList.stream()
                .forEach(product -> {
                    pw.println(orderDao.getProductFromOrderById(product.getId()).getQuantity() + "\t\t"
                            + product.getName() + "\t\t\t\t" + product.getPrice() + "\t"
                            + getProductStockCost(product.getId(), false)
                            * orderDao.getProductFromOrderById(product.getId()).getQuantity());
                    if (product.getStock() == 1 && numberOfProductsFromOrderWithStock(actualList) > 4) {
                        pw.println("\t(на товар \"" + product.getName() + "\" акция -10%)\t"
                                + BigDecimal.valueOf(getProductStockCost(product.getId(), true)
                                        * orderDao.getProductFromOrderById(product.getId()).getQuantity())
                                .setScale(2, RoundingMode.HALF_UP).doubleValue());
                    }
                });
    }

    @Override
    public void printEndingCheck(PrintWriter pw, int cardNumber) {
        pw.println("Сумма\t\t\t\t\t\t\t\t" + BigDecimal.valueOf(getTotalSum())
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
        pw.println("Скидка по предъявленной карте\t\t"
                + cardDao.getCardByNumber(cardNumber).getDiscount() + "%");
        pw.println("Сумма с учетом скидки\t\t\t\t"
                + BigDecimal.valueOf(getTotalSum() * ((100 - cardDao
                        .getCardByNumber(cardNumber).getDiscount())) / 100)
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
    }
}
