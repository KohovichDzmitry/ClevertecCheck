package ru.clevertec.check.service;

import ru.clevertec.check.annotation.Log;
import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.IProjectService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.handler.ProjectServiceHandler;

import java.io.PrintWriter;
import java.lang.reflect.Proxy;
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

    private IProjectService getProxyProjectService() {
        IProjectService iProjectService = this;
        ClassLoader productServiceClassLoader = iProjectService.getClass().getClassLoader();
        Class<?>[] productServiceInterfaces = iProjectService.getClass().getInterfaces();
        iProjectService = (IProjectService) Proxy.newProxyInstance(productServiceClassLoader,
                productServiceInterfaces, new ProjectServiceHandler(iProjectService));
        return iProjectService;
    }

    public void printProductFromTheOrder(PrintWriter pw) {
        CustomList<Product> actualList = this.getProxyProjectService().listProductsFromOrder();
        actualList.stream()
                .forEach(product -> {
                    pw.println(orderDao.getById(product.getId()).getQuantity() + "\t\t"
                            + product.getName() + "\t\t\t\t" + product.getPrice() + "\t"
                            + getProductStockCost(product.getId(), false)
                            * orderDao.getById(product.getId()).getQuantity());
                    if (product.getStock() == 1 && numberOfProductsFromOrderWithStock(actualList) > 4) {
                        pw.println("\t(на товар \"" + product.getName() + "\" акция -10%)\t"
                                + BigDecimal.valueOf(getProductStockCost(product.getId(), true)
                                        * orderDao.getById(product.getId()).getQuantity())
                                .setScale(2, RoundingMode.HALF_UP).doubleValue());
                    }
                });
    }

    public void printEndingCheck(PrintWriter pw, Long card_id) {
        pw.println("Сумма\t\t\t\t\t\t\t\t" + BigDecimal.valueOf(this.getProxyProjectService().getTotalSum())
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
        pw.println("Скидка по предъявленной карте\t\t"
                + cardDao.getById(card_id).getDiscount() + "%");
        pw.println("Сумма с учетом скидки\t\t\t\t"
                + BigDecimal.valueOf(getTotalSum() * ((100 - cardDao
                        .getById(card_id).getDiscount())) / 100)
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    @Log
    @Override
    public CustomList<Product> listProductsFromOrder() {
        return CustomList.toCustomList(Arrays
                .stream(orderDao.getAll().stream()
                        .mapToLong(Order::getId).toArray())
                .boxed().map(productDao::getById).toArray());
    }

    @Override
    public Long numberOfProductsFromOrderWithStock(CustomList<Product> customList) {
        return customList.stream()
                .filter(number -> number.getStock() == 1).count();
    }

    @Log
    @Override
    public Double getTotalSum() {
        return orderDao.getAll().stream()
                .map(product -> product.getQuantity() * getProductStockCost(product.getId(), true))
                .reduce(Double::sum)
                .orElseThrow(() -> new ServiceException("Неверный расчет стоимости заказа"));
    }

    @Override
    public Double getProductStockCost(Long id, Boolean mark) {
        CustomList<Product> actualList = listProductsFromOrder();
        return Optional.of(productDao.getById(id))
                .filter(product -> mark && product.getStock() == 1
                        && numberOfProductsFromOrderWithStock(actualList) > 4)
                .map(product -> product.getPrice() * ((100 - DISCOUNT_PERCENT) / 100))
                .orElse(productDao.getById(id).getPrice());
    }
}
