package ru.clevertec.check.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.check.dto.CardDto;
import ru.clevertec.check.entity.Card;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.mapper.CardMapper;
import ru.clevertec.check.repository.CardRepository;
import ru.clevertec.check.service.ICardService;
import ru.clevertec.check.validator.CardDataValidator;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService implements ICardService {

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
    public List<CardDto> findAll(Pageable pageable) {
        return cardRepository.findAll(pageable).stream().map(cardMapper::toDto).collect(toList());
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
    public CardDto findByNumber(Integer number) throws ServiceException {
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
