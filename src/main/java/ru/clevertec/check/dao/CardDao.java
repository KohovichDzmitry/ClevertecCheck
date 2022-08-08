package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;

public class CardDao implements ICardDao {

    private final CustomList<Card> listOfCards = new CustomArrayList<>();

    @Override
    public void saveCard(int number, int discount) {
        Card card = new Card(number, discount);
        listOfCards.add(card);
    }

    @Override
    public Card getCardByNumber(int number) {
        return listOfCards.stream()
                .filter(card -> card.getNumber() == number)
                .findFirst()
                .orElseThrow(() -> new DaoException("Не удалось определить предъявленную скидочную карту"));
    }
}
