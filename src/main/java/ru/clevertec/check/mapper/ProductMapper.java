package ru.clevertec.check.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.check.dto.ProductDto;
import ru.clevertec.check.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);
}
