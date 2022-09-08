package ru.clevertec.check.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.stereotype.Service;
import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.ICardService;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.validator.CardDataValidator;

import java.util.Map;

@Service
@Value
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardService extends AbstractService<Card, ICardDao> implements ICardService {

    ICardDao cardDao;

    @Override
    protected ICardDao getDao() {
        return cardDao;
    }

    @Override
    protected Card actionForSave(Map<String, String> parameters) throws ServiceException {
        Card card = new Card();
        if (!CardDataValidator.isValidCardParameters(parameters)) {
            throw new ServiceException("Ошибка при валидации данных скидочной карты");
        }
        card.setNumber(Integer.valueOf(parameters.get("card_number")));
        card.setDiscount(Integer.valueOf(parameters.get("discount")));
        return card;
    }

    @Override
    public Card getCardByNumber(Integer number) throws ServiceException {
        if (!CardDataValidator.isValidNumberCard(String.valueOf(number))) {
            throw new ServiceException("Ошибка при валидации номера скидочной карты. Доступные номера скидочных карт: " +
                    "0000, 1111, 2222, 3333, 4444 или 5555");
        }
        try {
            return cardDao.getCardByNumber(number);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
