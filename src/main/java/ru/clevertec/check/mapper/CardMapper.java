package ru.clevertec.check.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.check.dto.CardDto;
import ru.clevertec.check.entity.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardDto toDto(Card card);
}
