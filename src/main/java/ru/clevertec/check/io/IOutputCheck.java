package ru.clevertec.check.io;

import java.io.PrintWriter;

public interface IOutputCheck {

    void printCheck(Integer numberCard);

    Double findNeededProduct(PrintWriter pw);

    void discount(Integer numberCard, Double totalSum, PrintWriter pw);

    int stockProduct();
}
