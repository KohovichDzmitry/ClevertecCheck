package ru.clevertec.check.model.card;

public interface ICardDao {

    void saveCard(int number, int discount);

    Card getCardByNumber(int number);
}
