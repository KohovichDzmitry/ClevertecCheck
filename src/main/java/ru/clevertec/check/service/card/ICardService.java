package ru.clevertec.check.service.card;

import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.dto.CardDto;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.service.GenericService;

public interface ICardService extends GenericService<Card, CardDto> {

    CardDto findCardByNumber(Integer number) throws ServiceException;
}
