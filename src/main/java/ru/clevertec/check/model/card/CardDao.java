package ru.clevertec.check.model.card;

import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomIterator;
import ru.clevertec.check.util.CustomList;

public class CardDao implements ICardDao {

    private final CustomList<Card> listOfCards = new CustomArrayList<>();

    @Override
    public void save(int number, int discount) {
        Card card = new Card(number, discount);
        listOfCards.add(card);
    }

    @Override
    public Card getByNumber(int number) {
        CustomIterator<Card> cardCustomIterator = listOfCards.getIterator();
        while (cardCustomIterator.hasNext()) {
            Card card = cardCustomIterator.next();
            if (number == card.getNumber()) {
                return card;
            }
        }
        return null;
    }
}
