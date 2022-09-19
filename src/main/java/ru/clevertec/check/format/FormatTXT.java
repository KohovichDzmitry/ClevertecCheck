package ru.clevertec.check.format;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.service.order.IOrderService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

@Value
@RequiredArgsConstructor
public class FormatTXT implements Format {

    CustomList<Product> products;
    Integer discount;
    IOrderService orderService;

    @SneakyThrows
    public void setFormat() throws ServiceException {
        File file = new File("C:\\Users\\Dmitry\\IdeaProjects\\Clevertec\\src\\main\\resources\\print\\check");
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8)))) {
            pw.println("\t\t\t  " + NAME_OF_SHOP);
            pw.println("\t\t " + ADDRESS);
            pw.println("\t\t   " + PHONE_NUMBER + " \n");
            pw.println("\t " + CASHIER + " \t " + DATE + DATE_TIME.format(FORMATTER_DATE));
            pw.println("\t\t\t\t\t время: " + DATE_TIME.format(FORMATTER_TIME));
            pw.println("-----------------------------------------");
            pw.println(QUANTITY + "\t" + NAME + "\t\t" + PRICE + "\t" + SUM);
            products.stream()
                    .forEach(product -> {
                        pw.println(product.getQuantity() + "\t\t" + product.getName() + "\t\t\t\t" + product.getPrice()
                                + "\t" + BigDecimal.valueOf(orderService.getProductStockCost(products, product.getId(), false)
                                * product.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        if (product.getStock() == 1 && orderService.numberOfProductsFromOrderWithStock(products) > 4) {
                            pw.println("\t(на товар \"" + product.getName() + "\" акция -10%)\t"
                                    + BigDecimal.valueOf(orderService.getProductStockCost(products, product.getId(), true)
                                    * product.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        }
                    });
            pw.println("=========================================");
            pw.println(SUM + "\t\t\t\t\t\t\t\t" + BigDecimal.valueOf(orderService.getTotalSum(products))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
            pw.println(STOCK + "\t\t" + discount + "%");
            pw.println(SUM_WITH_STOCK + "\t\t\t\t" + BigDecimal.valueOf(orderService
                            .getTotalSum(products) * ((100 - discount)) / 100).setScale(2, RoundingMode.HALF_UP)
                    .doubleValue());
            pw.println("*****************************************");
            pw.println("\t\t\t" + THANKS);
        } catch (IOException e) {
            throw new ServiceException("Файл для записи чека не найден");
        }
    }
}
