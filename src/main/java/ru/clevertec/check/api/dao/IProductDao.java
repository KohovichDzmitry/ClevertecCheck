package ru.clevertec.check.api.dao;

import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.model.Product;

public interface IProductDao extends GenericDao<Product> {

    Product getProductByName(String name) throws DaoException;
}
