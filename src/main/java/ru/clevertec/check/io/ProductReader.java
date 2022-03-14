package ru.clevertec.check.io;

import ru.clevertec.check.model.product.ProductDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProductReader {

    public void read(ProductDao productDao) {
        File products = new File("src/main/resources/products");
        try (Scanner scanner = new Scanner(products)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(" ");
                String name = data[0];
                double price = Double.parseDouble(data[1]);
                int stock = Integer.parseInt(data[2]);
                productDao.save(name, price, stock);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл с продуктами не найден");
        }
    }
}
