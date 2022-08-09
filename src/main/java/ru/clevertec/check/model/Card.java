package ru.clevertec.check.model;

import java.util.Objects;

public class Card {

    private int number;
    private int discount;

    public Card() {
    }

    public Card(int number, int discount) {
        this.number = number;
        this.discount = discount;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return number == card.number && discount == card.discount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, discount);
    }

    @Override
    public String toString() {
        return "Card{" +
                "number=" + number +
                ", discount=" + discount + "%" +
                '}';
    }
}
