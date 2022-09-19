package ru.clevertec.check.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.clevertec.check.dto.CardDto;
import ru.clevertec.check.model.Card;

@Component
public class CardMapper {

    private final ModelMapper modelMapper;

    public CardMapper() {
        this.modelMapper = new ModelMapper();
    }

    public CardDto toDto(Card card) {
        return modelMapper.map(card, CardDto.class);
    }
}
