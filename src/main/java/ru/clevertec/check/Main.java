package ru.clevertec.check;

import ru.clevertec.check.io.*;
import ru.clevertec.check.model.card.CardDao;
import ru.clevertec.check.model.card.ICardDao;
import ru.clevertec.check.model.checkRunner.CheckRunnerDao;
import ru.clevertec.check.model.checkRunner.ICheckRunnerDao;
import ru.clevertec.check.model.product.IProductDao;
import ru.clevertec.check.model.product.ProductDao;

public class Main {

    private static final IProductDao productDao = new ProductDao();
    private static final ICardDao cardDao = new CardDao();
    private static final ICheckRunnerDao checkRunnerDao = new CheckRunnerDao();
    private static final IOutputCheck outputCheck = new OutputCheck(productDao, cardDao, checkRunnerDao);

    public static void main(String[] args) {

        ProductReader productReader = new ProductReader();
        productReader.read((ProductDao) productDao);
        //создали и сохранили 20 продуктов из файла products; stock: 1 - товар на акции, 0 - не на акции

        CardReader cardReader = new CardReader();
        cardReader.read((CardDao) cardDao);
        // создали и сохранили 5 скидочных карт из файла cards

        CheckRunnerReader checkRunnerReader = new CheckRunnerReader();
        checkRunnerReader.read((CheckRunnerDao) checkRunnerDao);
        //создали и сохранили содержимое чека (для примера) из файла checkRunner

        outputCheck.printCheck(4444);
        //печать чека в файл check, с учетом скидки по предьявленной карте
    }
}
