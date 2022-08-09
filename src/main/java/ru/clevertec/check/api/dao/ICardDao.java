package ru.clevertec.check.api.dao;

import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Card;

public interface ICardDao {

    Card saveCard(Card card);

    Card getCardByNumber(Integer number);

    CustomList<Card> getAllCards();

    Card updateDiscountInCard(Integer number, Integer discount);

    void deleteCard(Integer number);
}
