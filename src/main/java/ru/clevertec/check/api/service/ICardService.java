package ru.clevertec.check.api.service;

import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.model.Card;

public interface ICardService extends GenericService<Card> {

    Card getCardByNumber(Integer number) throws ServiceException;
}
