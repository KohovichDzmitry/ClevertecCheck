package ru.clevertec.check.service;

import ru.clevertec.check.annotation.Log;
import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.dao.IOrderDao;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.IOrderService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.CardDao;
import ru.clevertec.check.dao.OrderDao;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.handler.OrderServiceHandler;
import ru.clevertec.check.validator.OrderDataValidator;

import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class OrderService extends AbstractService<Order, IOrderDao> implements IOrderService {

    private static final OrderService INSTANCE = new OrderService();
    private final IProductDao productDao = ProductDao.getInstance();
    private final ICardDao cardDao = CardDao.getInstance();
    private final IOrderDao orderDao = OrderDao.getInstance();

    private static final Double DISCOUNT_PERCENT = 10d;

    private OrderService() {
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    @Override
    protected IOrderDao getDao() {
        return orderDao;
    }

    @Override
    protected Order actionForSave(Map<String, String> parameters) throws ServiceException {
        if (!OrderDataValidator.isValidOrderParameters(parameters)) {
            throw new ServiceException("Ошибка при валидации данных заказа");
        }
        try {
            Product product = productDao.getById(Long.valueOf(parameters.get("id_product")));
            Order order = new Order();
            order.setProduct(product);
            order.setQuantity(Integer.valueOf(parameters.get("quantity")));
            return order;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private IOrderService getProxyOrderService() {
        IOrderService iOrderService = this;
        ClassLoader productServiceClassLoader = iOrderService.getClass().getClassLoader();
        Class<?>[] productServiceInterfaces = iOrderService.getClass().getInterfaces();
        iOrderService = (IOrderService) Proxy.newProxyInstance(productServiceClassLoader,
                productServiceInterfaces, new OrderServiceHandler(iOrderService));
        return iOrderService;
    }

    @Override
    public void printProductFromTheOrder(PrintWriter pw) throws ServiceException {
        CustomList<Product> actualList = this.getProxyOrderService().listProductsFromOrder();
        try {
            actualList.stream()
                    .forEach(product -> {
                        pw.println(orderDao.getOrderByIdProduct(product.getId()).getQuantity() + "\t\t"
                                + product.getName() + "\t\t\t\t" + product.getPrice() + "\t"
                                + getProductStockCost(product.getId(), false)
                                * orderDao.getOrderByIdProduct(product.getId()).getQuantity());
                        if (product.getStock() == 1 && numberOfProductsFromOrderWithStock(actualList) > 4) {
                            pw.println("\t(на товар \"" + product.getName() + "\" акция -10%)\t"
                                    + BigDecimal.valueOf(getProductStockCost(product.getId(), true)
                                            * orderDao.getOrderByIdProduct(product.getId()).getQuantity())
                                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
                        }
                    });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void printEndingCheck(PrintWriter pw, Long card_id) throws ServiceException {
        try {
            pw.println("Сумма\t\t\t\t\t\t\t\t" + BigDecimal.valueOf(this.getProxyOrderService().getTotalSum())
                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
            pw.println("Скидка по предъявленной карте\t\t"
                    + cardDao.getById(card_id).getDiscount() + "%");
            pw.println("Сумма с учетом скидки\t\t\t\t"
                    + BigDecimal.valueOf(getTotalSum() * ((100 - cardDao
                            .getById(card_id).getDiscount())) / 100)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Order getOrderByIdProduct(Long id) throws ServiceException {
        if (!OrderDataValidator.isValidProductIDInOrder(String.valueOf(id))) {
            throw new ServiceException("Ошибка при валидации id продукта из заказа");
        }
        try {
            return orderDao.getOrderByIdProduct(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Log
    @Override
    public CustomList<Product> listProductsFromOrder() throws ServiceException {
        try {
            return CustomList.toCustomList(Arrays
                    .stream(orderDao.getAll().stream()
                            .mapToLong(order -> order.getProduct().getId()).toArray())
                    .boxed().map(productDao::getById).toArray());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Long numberOfProductsFromOrderWithStock(CustomList<Product> customList) {
        return customList.stream()
                .filter(number -> number.getStock() == 1).count();
    }

    @Log
    @Override
    public Double getTotalSum() throws ServiceException {
        try {
            return orderDao.getAll().stream()
                    .map(product -> product.getQuantity() * getProductStockCost(product.getProduct().getId(), true))
                    .reduce(Double::sum)
                    .orElseThrow(() -> new ServiceException("Неверный расчет стоимости заказа"));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Double getProductStockCost(Long id, Boolean mark) throws ServiceException {
        CustomList<Product> actualList = listProductsFromOrder();
        try {
            return Optional.of(productDao.getById(id))
                    .filter(product -> mark && product.getStock() == 1
                            && numberOfProductsFromOrderWithStock(actualList) > 4)
                    .map(product -> product.getPrice() * ((100 - DISCOUNT_PERCENT) / 100))
                    .orElse(productDao.getById(id).getPrice());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
