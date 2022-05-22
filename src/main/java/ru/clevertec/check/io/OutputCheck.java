package ru.clevertec.check.io;

import ru.clevertec.check.model.card.Card;
import ru.clevertec.check.model.card.ICardDao;
import ru.clevertec.check.model.order.Order;
import ru.clevertec.check.model.order.IOrderDao;
import ru.clevertec.check.model.product.IProductDao;
import ru.clevertec.check.model.product.Product;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.util.CustomIterator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;

public class OutputCheck implements IOutputCheck {

    private final IProductDao productDao;
    private final ICardDao cardDao;
    private final IOrderDao orderDao;
    private final ProductService productService;
    private final LocalDate date = LocalDate.now();
    private final LocalTime time = LocalTime.now();

    public OutputCheck(IProductDao productDao, ICardDao cardDao, IOrderDao orderDao) {
        this.productDao = productDao;
        this.cardDao = cardDao;
        this.orderDao = orderDao;
        this.productService = new ProductService(orderDao, productDao);
    }

    @Override
    public void printCheck(int numberCard) {
        File file = new File("src/main/resources/check");
        try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
            Card card = cardDao.getByNumber(numberCard);
            pw.println("\t\t\t  -=Магазин 777=-");
            pw.println("\t\t г.Минск, ул. Макаёнка 99");
            pw.println("\t\t   тел. 8017-123-45-67 \n");
            pw.println("\t кассир: №1234 \t дата: " + date);
            pw.println("\t\t\t\t\t время: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
            pw.println("-----------------------------------------");
            pw.println("Кол.\t" + "Наименование\t\t" + "Цена\t" + "Сумма");

            productService.printProductFromTheOrder(pw);

            double totalSum = BigDecimal.valueOf(findNeededProduct(pw))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            pw.println("=========================================");
            pw.println("Сумма\t\t\t\t\t\t\t\t" + totalSum);
            discount(card.getDiscount(), totalSum, pw);
            pw.println("*****************************************");
            pw.println("\t\t\tCпасибо за покупку!");
        } catch (NullPointerException e) {
            System.out.println("Не удалось определить предъявленную скидочную карту (если она есть)");
        } catch (IOException e) {
            System.out.println("Файл для записи чека не найден");
        }
    }

    @Override
    public void discount(int discount, double totalSum, PrintWriter pw) {
        if (discount != 0) {
            pw.println("Скидка по предъявленной карте\t\t" + discount + "%");
            pw.println("Сумма с учетом скидки\t\t\t\t" + BigDecimal.valueOf(totalSum
                    * ((100 - discount)) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
    }

    @Override
    public double findNeededProduct(PrintWriter pw) {
        try {
            double totalSum = 0;
            CustomIterator<Order> checkRunnerCustomIterator = orderDao.getAll().getIterator();
            while (checkRunnerCustomIterator.hasNext()) {

                Order productInCheck = checkRunnerCustomIterator.next();

                Product productInShop = productDao.getById(productInCheck.getId());
                double sum = productInShop.getPrice() * productInCheck.getQuantity();
                String field = productInCheck.getQuantity() + "\t\t" + productInShop.getName() + "\t\t\t\t" +
                        productInShop.getPrice() + "\t" + sum;
                long numberOfProductsInStock = productDao.getAll().stream()
                        .filter(e -> e.getStock() == 1).count();
                if (numberOfProductsInStock > 4 && productInShop.getStock() == 1) {
                    pw.println(field);
                    sum = productInShop.getPrice() * productInCheck.getQuantity() * 0.9;
                    pw.println("\t(на товар \"" + productInShop.getName() + "\" акция -10%)\t"
                            + BigDecimal.valueOf(sum).setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    pw.println(field);
                }
                totalSum = totalSum + sum;
            }
            return totalSum;
        } catch (NullPointerException e) {
            throw new NullPointerException("Товара с выбранным id не существует");
        }
    }
}
