package ru.clevertec.check.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.clevertec.check.dto.ProductDto;
import ru.clevertec.check.model.Product;

@Component
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductMapper() {
        this.modelMapper = new ModelMapper();
    }

    public ProductDto toDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
