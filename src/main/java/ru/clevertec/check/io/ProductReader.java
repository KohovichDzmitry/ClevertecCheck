package ru.clevertec.check.io;

import ru.clevertec.check.model.product.IProductDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ProductReader {

    public void read(IProductDao productDao) {
        File products = new File("src/main/resources/products");
        File invalidData = new File("src/main/resources/invalidData.txt");
        try (Scanner scanner = new Scanner(products, StandardCharsets.UTF_8)) {
            FileWriter fw = new FileWriter(invalidData, false);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(" ");
                String regex = "(([А-ЯЁ][а-яё]{2,29})|([A-Z][a-z]{2,29}))\\s" +
                        "((([1-9]|[1-9]\\d)\\.[0-9]{2})|(100\\.00))\\s([10])";
                if (Pattern.matches(regex, line)) {
                    String name = data[0];
                    double price = Double.parseDouble(data[1]);
                    int stock = Integer.parseInt(data[2]);
                    productDao.save(name, price, stock);
                } else {
                    fw.write(line + "\n");
                }
            }
            fw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл с продуктами не найден");
        } catch (IOException e) {
            System.out.println("Файл для записи неверных данных не найден");
        }
    }
}
