package ru.clevertec.check.model.card;

public class Card {

    private final Integer number;
    private final Integer discount;

    public Card(Integer number, Integer discount) {
        this.number = number;
        this.discount = discount;
    }

    public int getNumber() {
        return number;
    }

    public Integer getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        return "Card{" +
                "number=" + number +
                "discount=" + discount + "%" +
                '}';
    }
}
