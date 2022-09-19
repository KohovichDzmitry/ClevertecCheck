package ru.clevertec.check.service.product;

import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dto.ProductDto;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.GenericService;

public interface IProductService extends GenericService<Product, ProductDto> {

    ProductDto findProductByName(String name) throws ServiceException;

    CustomList<ProductDto> findAllAndOrderByName();

    CustomList<ProductDto> findAllAndOrderByPrice();
}
