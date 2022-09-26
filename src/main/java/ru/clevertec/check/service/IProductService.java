package ru.clevertec.check.service;

import ru.clevertec.check.dto.ProductDto;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exceptions.ServiceException;

import java.util.List;

public interface IProductService extends GenericService<Product, ProductDto> {

    ProductDto findByName(String name) throws ServiceException;

    List<ProductDto> findAllAndOrderByName();

    List<ProductDto> findAllAndOrderByPrice();
}
