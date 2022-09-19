package ru.clevertec.check.service;

import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;

public interface GenericService<T, D> {

    D save(T entity) throws ServiceException;

    D findById(Long id) throws ServiceException;

    CustomList<D> getAll();

    CustomList<D> findAll(String pageSizeStr, String pageStr);

    D update(T entity, Long id) throws ServiceException;

    void delete(Long id) throws ServiceException;

    Integer countAllEntities();
}
