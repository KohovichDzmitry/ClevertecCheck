package ru.clevertec.check.model.card;

import ru.clevertec.check.util.CustomList;

public interface ICardDao {

    void saveCard(int number, int discount);

    Card getCardByNumber(int number);

    CustomList<Card> getAllCards();
}
