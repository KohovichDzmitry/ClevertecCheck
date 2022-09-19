package ru.clevertec.check.service.card;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dto.CardDto;
import ru.clevertec.check.mapper.CardMapper;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.repository.CardRepository;
import ru.clevertec.check.validator.CardDataValidator;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService implements ICardService {

    @Value("${check.pageSizeDefault}")
    private int pageSize;
    @Value("${check.pageDefault}")
    private int page;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public CardDto save(Card card) throws ServiceException {
        if (!CardDataValidator.isValidCardParameters(card)) {
            throw new ServiceException("Ошибка при валидации данных скидочной карты");
        }
        return cardMapper.toDto(cardRepository.save(card));
    }

    @Override
    public CardDto findById(Long id) throws ServiceException {
        return cardRepository.findById(id).map(cardMapper::toDto)
                .orElseThrow(() -> new ServiceException(String
                        .format("Ошибка при попытке найти скидочную карту по введённому id: %d", id)));
    }

    @Override
    public CustomList<CardDto> getAll() {
        CustomList<CardDto> cards = new CustomArrayList<>();
        cardRepository.findAll().stream().map(cardMapper::toDto).forEach(cards::add);
        return cards;
    }

    @Override
    public CustomList<CardDto> findAll(String pageSizeStr, String pageStr) {
        if (pageSizeStr != null) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }
        CustomList<CardDto> cards = new CustomArrayList<>();
        cardRepository.findAll(PageRequest.of(page, pageSize)).stream()
                .map(cardMapper::toDto).forEach(cards::add);
        return cards;
    }

    @Override
    @Transactional
    public CardDto update(Card card, Long id) throws ServiceException {
        if (cardRepository.findById(id).isEmpty()) {
            throw new ServiceException(String.format("Ошибка при попытке найти скидочную карту по введённому id: %d", id));
        }
        if (!CardDataValidator.isValidCardParameters(card)) {
            throw new ServiceException("Ошибка при валидации данных скидочной карты");
        }
        card.setId(id);
        return cardMapper.toDto(cardRepository.save(card));
    }

    @Override
    @Transactional
    public void delete(Long id) throws ServiceException {
        if (cardRepository.findById(id).isEmpty()) {
            throw new ServiceException(String.format("Ошибка при попытке найти скидочную карту по введённому id: %d", id));
        }
        cardRepository.deleteById(id);
    }

    @Override
    public CardDto findCardByNumber(Integer number) throws ServiceException {
        if (!CardDataValidator.isValidNumberCard(String.valueOf(number))) {
            throw new ServiceException("Ошибка при валидации номера скидочной карты. Доступные номера скидочных карт: " +
                    "0000, 1111, 2222, 3333, 4444 или 5555");
        }
        return cardRepository.findCardByNumber(number).map(cardMapper::toDto)
                .orElseThrow(() -> new ServiceException(String
                        .format("Ошибка при попытке найти скидочную карту по введённому номеру: %d", number)));
    }

    @Override
    public Integer countAllEntities() {
        return cardRepository.countAllBy();
    }
}
