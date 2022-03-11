package ru.clevertec.check.io;

import ru.clevertec.check.model.checkRunner.CheckRunnerDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CheckRunnerReader {

    public void read(CheckRunnerDao checkRunnerDao) {
        File checkRunner = new File("src/main/resources/checkRunner");
        try (Scanner scanner = new Scanner(checkRunner)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(" ");
                Integer id = Integer.parseInt(data[0]);
                Integer quantity = Integer.parseInt(data[1]);
                checkRunnerDao.buy(id, quantity);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл с товарами в чеке не найден");
        }
    }
}
