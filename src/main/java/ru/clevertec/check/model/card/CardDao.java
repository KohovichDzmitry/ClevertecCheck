package ru.clevertec.check.model.card;

import java.util.ArrayList;
import java.util.List;

public class CardDao implements ICardDao{

    private final List<Card> listOfCards = new ArrayList<>();

    @Override
    public void save(int number, int discount) {
        Card card = new Card(number, discount);
        listOfCards.add(card);
    }

    @Override
    public Card getByNumber(int number) {
        for (Card card : listOfCards) {
            if (number == card.getNumber()) {
                return card;
            }
        }
        return null;
    }
}
