package ru.clevertec.check.api.dao;

import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.model.Card;

public interface ICardDao extends GenericDao<Card> {

    Card getCardByNumber(Integer number) throws DaoException;
}
