package ru.clevertec.check.api.service;

import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;

public interface IProductService extends GenericService<Product> {

    Product getProductByName(String name) throws ServiceException;

    CustomList<Product> getAllSortedByAlphabet() throws ServiceException;

    CustomList<Product> getAllSortedByPrice() throws ServiceException;
}
