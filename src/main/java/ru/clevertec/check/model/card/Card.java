package ru.clevertec.check.model.card;

public class Card {

    private final int number;
    private final int discount;

    public Card(int number, int discount) {
        this.number = number;
        this.discount = discount;
    }

    public int getNumber() {
        return number;
    }

    public int getDiscount() {
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
