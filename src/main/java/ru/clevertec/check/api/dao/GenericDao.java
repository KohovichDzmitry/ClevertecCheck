package ru.clevertec.check.api.dao;

import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.AEntity;

import java.util.Comparator;

public interface GenericDao<T extends AEntity> {

    T save(T entity) throws DaoException;

    T getById(Long id) throws DaoException;

    CustomList<T> getAll() throws DaoException;

    CustomList<T> findAll(Integer pageSize, Integer page) throws DaoException;

    T update(T entity, Long id) throws DaoException;

    void delete(Long id) throws DaoException;

    CustomList<T> getAllSorted(Comparator<T> comparator) throws DaoException;
}
