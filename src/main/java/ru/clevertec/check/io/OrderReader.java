package ru.clevertec.check.io;

import ru.clevertec.check.model.order.IOrderDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;

public class OrderReader {

    public void read(IOrderDao orderDao) {
        File checkRunner = new File("src/main/resources/order");
        File invalidData = new File("src/main/resources/invalidData.txt");
        try (Scanner scanner = new Scanner(checkRunner, StandardCharsets.UTF_8)) {
            FileWriter fw = new FileWriter(invalidData, true);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(" ");
                String regex = "([1-9]|[1-9]\\d|100)\\s([1-9]|1\\d|20)";
                if (Pattern.matches(regex, line)) {
                    int id = Integer.parseInt(data[0]);
                    int quantity = Integer.parseInt(data[1]);
                    orderDao.buyOrder(id, quantity);
                } else {
                    fw.write(line + "\n");
                }
            }
            fw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл с товарами в чеке не найден");
        } catch (IOException e) {
            System.out.println("Файл для записи неверных данных не найден");
        }
    }
}
