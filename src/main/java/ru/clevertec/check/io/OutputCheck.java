package ru.clevertec.check.io;

import ru.clevertec.check.service.ProjectService;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;

public class OutputCheck {

    private final ProjectService projectService;
    private final LocalDate date = LocalDate.now();
    private final LocalTime time = LocalTime.now();

    public OutputCheck(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void printCheck(Long card_id) {
        File file = new File("src/main/resources/check");
        try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
            pw.println("\t\t\t  -=Магазин 777=-");
            pw.println("\t\t г.Минск, ул. Макаёнка 99");
            pw.println("\t\t   тел. 8017-123-45-67 \n");
            pw.println("\t кассир: №1234 \t дата: " + date);
            pw.println("\t\t\t\t\t время: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
            pw.println("-----------------------------------------");
            pw.println("Кол.\t" + "Наименование\t\t" + "Цена\t" + "Сумма");
            projectService.printProductFromTheOrder(pw);
            pw.println("=========================================");
            projectService.printEndingCheck(pw, card_id);
            pw.println("*****************************************");
            pw.println("\t\t\tCпасибо за покупку!");
        } catch (IOException e) {
            System.out.println("Файл для записи чека не найден");
        }
    }
}
