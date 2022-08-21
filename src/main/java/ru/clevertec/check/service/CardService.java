package ru.clevertec.check.service;

import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.ICardService;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.validator.CardDataValidator;

import java.util.Map;

public class CardService extends AbstractService<Card, ICardDao> implements ICardService {

    private final ICardDao cardDao;

    public CardService(ICardDao cardDao) {
        this.cardDao = cardDao;
    }

    @Override
    protected ICardDao getDao() {
        return cardDao;
    }

    @Override
    protected Card actionForSave(Map<String, String> parameters) {
        Card card = new Card();
        if (CardDataValidator.isValidCardParameters(parameters)) {
            card.setNumber(Integer.valueOf(parameters.get("card_number")));
            card.setDiscount(Integer.valueOf(parameters.get("discount")));
            return card;
        } else {
            throw new ServiceException("Ошибка при валидации данных скидочной карты");
        }
    }
}
