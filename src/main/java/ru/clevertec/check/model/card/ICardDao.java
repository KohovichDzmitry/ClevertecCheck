package ru.clevertec.check.model.card;

public interface ICardDao {

    void save(int number, int discount);

    Card getByNumber(int number);
}
