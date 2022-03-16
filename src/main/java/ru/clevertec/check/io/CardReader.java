package ru.clevertec.check.io;

import ru.clevertec.check.model.card.CardDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CardReader {

    public void read(CardDao cardDao) {
        File cards = new File("src/main/resources/cards");
        File invalidData = new File("src/main/resources/invalidData.txt");
        try (Scanner scanner = new Scanner(cards, StandardCharsets.UTF_8)) {
            FileWriter fw = new FileWriter(invalidData, true);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(" ");
                String regex = "0000\\s0|1111\\s1|2222\\s2|3333\\s3|4444\\s4|5555\\s5";
                if (Pattern.matches(regex, line)) {
                    int number = Integer.parseInt(data[0]);
                    int discount = Integer.parseInt(data[1]);
                    cardDao.save(number, discount);
                } else {
                    fw.write(line + "\n");
                }
            }
            fw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл со скидочными картами не найден");
        } catch (IOException e) {
            System.out.println("Файл для записи неверных данных не найден");
        }
    }
}
