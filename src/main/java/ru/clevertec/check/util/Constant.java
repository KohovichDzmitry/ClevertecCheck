package ru.clevertec.check.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Constant {

    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final LocalDateTime DATE_TIME = LocalDateTime.now();
    public static final String EMPTY = "";
    public static final String NAME_OF_SHOP = "-=Магазин 777=-";
    public static final String ADDRESS = "г.Минск, ул. Макаёнка 99";
    public static final String PHONE_NUMBER = "тел. 8017-123-45-67";
    public static final String CASHIER = "кассир: №1234";
    public static final String DATE = "дата: ";
    public static final String TIME = "время: ";
    public static final String QUANTITY = "Кол.";
    public static final String NAME = "Наименование";
    public static final String PRICE = "Цена";
    public static final String SUM = "Сумма";
    public static final String STOCK = "Скидка по предъявленной карте";
    public static final String SUM_WITH_STOCK = "Сумма с учетом скидки";
    public static final String THANKS = "Cпасибо за покупку!";
}
