package ru.clevertec.check.service;

import ru.clevertec.check.dto.CardDto;
import ru.clevertec.check.entity.Card;
import ru.clevertec.check.exceptions.ServiceException;

public interface ICardService extends GenericService<Card, CardDto> {

    CardDto findByNumber(Integer number) throws ServiceException;
}
