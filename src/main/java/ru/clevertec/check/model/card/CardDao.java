package ru.clevertec.check.model.card;

import ru.clevertec.check.util.CustomArrayList;
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
        return listOfCards.stream()
                .filter(card -> card.getNumber() == number)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Не удалось определить предъявленную скидочную карту"));
    }
}
