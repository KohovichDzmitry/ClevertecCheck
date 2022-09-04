package ru.clevertec.check.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import ru.clevertec.check.annotation.Log;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.ICardService;
import ru.clevertec.check.api.service.IOrderService;
import ru.clevertec.check.api.service.IProductService;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.format.FormatPDF;
import ru.clevertec.check.format.FormatTXT;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.handler.OrderServiceHandler;
import ru.clevertec.check.validator.ProductDataValidator;

import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Optional;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderService implements IOrderService {

    private static final OrderService INSTANCE = new OrderService();
    IProductService productService = ProductService.getInstance();
    ICardService cardService = CardService.getInstance();
    private static final Double DISCOUNT_PERCENT = 10d;

    public static OrderService getInstance() {
        return INSTANCE;
    }

    @Override
    public void printCheck(Map<String, String[]> map, OutputStream out) throws ServiceException {
        try {
            String[] arrayId = map.get("id");
            String[] arrayCount = map.get("count");
            String[] arrayCard = map.get("card");
            CustomList<Product> products = new CustomArrayList<>();
            for (String s : arrayId) {
                Product product = productService.getById(Long.parseLong(s));
                products.add(product);
            }
            Card card = cardService.getCardByNumber(Integer.valueOf(arrayCard[0]));
            this.getProxyOrderService().parseOrder(products, arrayCount);
            FormatPDF formatPDF = new FormatPDF(products, card.getDiscount(), out);
            formatPDF.setFormat();
            FormatTXT formatTXT = new FormatTXT(products, card.getDiscount());
            formatTXT.setFormat();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Log
    @Override
    public void parseOrder(CustomList<Product> products, String[] arrayCount) throws ServiceException {
        for (int i = 0; i < products.size(); i++) {
            if (ProductDataValidator.isValidQuantityProduct(arrayCount[i])) {
                int quantity = Integer.parseInt(arrayCount[i]);
                products.get(i).setQuantity(quantity);
            } else {
                throw new ServiceException("Ошибка при валидации количества продукта. Количество продукта должно быть "
                        + "больше нуля");
            }
        }
    }

    private IOrderService getProxyOrderService() {
        IOrderService iOrderService = this;
        ClassLoader orderServiceClassLoader = iOrderService.getClass().getClassLoader();
        Class<?>[] orderServiceInterfaces = iOrderService.getClass().getInterfaces();
        iOrderService = (IOrderService) Proxy.newProxyInstance(orderServiceClassLoader,
                orderServiceInterfaces, new OrderServiceHandler());
        return iOrderService;
    }

    @Override
    public Double getProductStockCost(CustomList<Product> products, Long id, Boolean mark) throws ServiceException {
        try {
            return Optional.of(productService.getById(id))
                    .filter(product -> mark && product.getStock() == 1
                            && numberOfProductsFromOrderWithStock(products) > 4)
                    .map(product -> product.getPrice() * ((100 - DISCOUNT_PERCENT) / 100))
                    .orElse(productService.getById(id).getPrice());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Long numberOfProductsFromOrderWithStock(CustomList<Product> customList) {
        return customList.stream()
                .filter(number -> number.getStock() == 1).count();
    }

    @Override
    public Double getTotalSum(CustomList<Product> products) throws ServiceException {
        try {
            return products.stream()
                    .map(product -> product.getQuantity() * getProductStockCost(products, product.getId(), true))
                    .reduce(Double::sum)
                    .orElseThrow(() -> new ServiceException("Неверный расчет стоимости заказа"));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
