package ru.clevertec.check.model.card;

public interface ICardDao {

    void save(Integer number, Integer discount);

    Card getByNumber(Integer number);
}
