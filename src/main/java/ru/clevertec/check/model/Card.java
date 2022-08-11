package ru.clevertec.check.model;

import java.util.Objects;

public class Card extends AEntity {

    private Integer number;
    private Integer discount;

    public Card() {
    }

    public Card(Integer number, Integer discount) {
        this.number = number;
        this.discount = discount;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(number, card.number) && Objects.equals(discount, card.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, discount);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + getId() +
                " number=" + number +
                ", discount=" + discount +
                '}';
    }
}
