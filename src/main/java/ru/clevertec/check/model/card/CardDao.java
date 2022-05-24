package ru.clevertec.check.model.card;

import ru.clevertec.check.exception.ProjectException;
import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomList;

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
                .orElseThrow(() -> new ProjectException("Не удалось определить предъявленную скидочную карту"));
    }

    @Override
    public CustomList<Card> getAllCards() {
        return new CustomArrayList<>(listOfCards);
    }
}
