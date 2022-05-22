package ru.clevertec.check.io;

import java.io.PrintWriter;

public interface IOutputCheck {

    void printCheck(int numberCard);

    double findNeededProduct(PrintWriter pw);

    void discount(int numberCard, double totalSum, PrintWriter pw);
}
