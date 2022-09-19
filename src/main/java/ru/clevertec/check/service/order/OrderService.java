package ru.clevertec.check.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.check.annotation.Log;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.format.FormatPDF;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.CardRepository;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.service.handler.OrderServiceHandler;
import ru.clevertec.check.validator.ProductDataValidator;

import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final ProductRepository productRepository;
    private final CardRepository cardRepository;
    @Value("${check.discountPercent}")
    private String discountPercent;

    @Override
    public void printCheck(Map<String, String[]> map, OutputStream out) throws ServiceException {
        String[] arrayId = map.get("id");
        String[] arrayCount = map.get("count");
        String[] arrayCard = map.get("card");
        CustomList<Product> products = new CustomArrayList<>();
        for (String s : arrayId) {
            productRepository.findById(Long.parseLong(s)).ifPresent(products::add);
        }
        Optional<Card> card = cardRepository.findCardByNumber(Integer.valueOf(arrayCard[0])).stream().findFirst();
        this.getProxyOrderService().parseOrder(products, arrayCount);
        FormatPDF formatPDF = new FormatPDF(products, card.get().getDiscount(), this, out);
        formatPDF.setFormat();
//            FormatTXT formatTXT = new FormatTXT(products, card.getDiscount(), this);
//            formatTXT.setFormat();
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
                orderServiceInterfaces, new OrderServiceHandler(this));
        return iOrderService;
    }

    @Override
    public Double getProductStockCost(CustomList<Product> products, Long id, Boolean mark) throws ServiceException {
        return Optional.of(productRepository.findById(id))
                .filter(product -> mark && product.get().getStock() == 1
                        && numberOfProductsFromOrderWithStock(products) > 4)
                .map(product -> product.get().getPrice() * ((100 - Double.parseDouble(discountPercent)) / 100))
                .orElse(productRepository.findById(id).get().getPrice());
    }

    @Override
    public Long numberOfProductsFromOrderWithStock(CustomList<Product> customList) {
        return customList.stream()
                .filter(number -> number.getStock() == 1).count();
    }

    @Override
    public Double getTotalSum(CustomList<Product> products) throws ServiceException {
        return products.stream()
                .map(product -> product.getQuantity() * getProductStockCost(products, product.getId(), true))
                .reduce(Double::sum)
                .orElseThrow(() -> new ServiceException("Неверный расчет стоимости заказа"));
    }
}
