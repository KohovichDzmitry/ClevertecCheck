package ru.clevertec.check.api.dao;

import ru.clevertec.check.model.Card;

public interface ICardDao {

    void saveCard(int number, int discount);

    Card getCardByNumber(int number);
}
