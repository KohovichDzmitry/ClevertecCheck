package ru.clevertec.check.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.check.annotation.Log;
import ru.clevertec.check.entity.Card;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.repository.CardRepository;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.service.IOrderService;
import ru.clevertec.check.service.handler.OrderServiceHandler;
import ru.clevertec.check.validator.ProductDataValidator;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    @Value("${check.discountPercent}")
    private final String discountPercent;
    @Value("${check.id}")
    private final String id;
    @Value("${check.count}")
    private final String count;
    @Value("${check.card}")
    private final String card;
    private final ProductRepository productRepository;
    private final CardRepository cardRepository;

    @Override
    public CheckInfo readCheck(Map<String, String[]> map) throws ServiceException {
        String[] arrayId = map.get(id);
        String[] arrayCount = map.get(count);
        String[] arrayCard = map.get(card);
        List<Product> products = new ArrayList<>();
        for (String s : arrayId) {
            productRepository.findById(Long.parseLong(s)).ifPresent(products::add);
        }
        Optional<Card> card = cardRepository.findCardByNumber(Integer.valueOf(arrayCard[0])).stream().findFirst();
        this.getProxyOrderService().parseOrder(products, arrayCount);
        return new CheckInfo(products, card.get().getDiscount());
    }

    @Log
    @Override
    public void parseOrder(List<Product> products, String[] arrayCount) throws ServiceException {
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
                orderServiceInterfaces, new OrderServiceHandler(this));
        return iOrderService;
    }

    @Override
    public Double getProductStockCost(List<Product> products, Long id, Boolean mark) throws ServiceException {
        return Optional.of(productRepository.findById(id))
                .filter(product -> mark && product.get().getStock() == 1
                        && numberOfProductsFromOrderWithStock(products) > 4)
                .map(product -> product.get().getPrice() * ((100 - Double.parseDouble(discountPercent)) / 100))
                .orElse(productRepository.findById(id).get().getPrice());
    }

    @Override
    public Long numberOfProductsFromOrderWithStock(List<Product> customList) {
        return customList.stream()
                .filter(number -> number.getStock() == 1).count();
    }

    @Override
    public Double getTotalSum(List<Product> products) throws ServiceException {
        return products.stream()
                .map(product -> product.getQuantity() * getProductStockCost(products, product.getId(), true))
                .reduce(Double::sum)
                .orElseThrow(() -> new ServiceException("Неверный расчет стоимости заказа"));
    }
}
