package ru.clevertec.check.format;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface Format {

    DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalDateTime DATE_TIME = LocalDateTime.now();
    String EMPTY = "";
    String NAME_OF_SHOP = "-=Магазин 777=-";
    String ADDRESS = "г.Минск, ул. Макаёнка 99";
    String PHONE_NUMBER = "тел. 8017-123-45-67";
    String CASHIER = "кассир: №1234";
    String DATE = "дата: ";
    String TIME = "время: ";
    String QUANTITY = "Кол.";
    String NAME = "Наименование";
    String PRICE = "Цена";
    String SUM = "Сумма";
    String STOCK = "Скидка по предъявленной карте";
    String SUM_WITH_STOCK = "Сумма с учетом скидки";
    String THANKS = "Cпасибо за покупку!";

    void setFormat();
}
