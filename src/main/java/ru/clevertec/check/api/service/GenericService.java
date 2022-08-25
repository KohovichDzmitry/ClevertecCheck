package ru.clevertec.check.api.service;

import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.AEntity;

import java.util.Map;

public interface GenericService<T extends AEntity> {

    T save(Map<String, String> parameters) throws ServiceException;

    T getById(Long id) throws ServiceException;

    CustomList<T> getAll() throws ServiceException;

    CustomList<T> findAll(String pageSizeStr, String pageStr) throws ServiceException;

    T update(Map<String, String> parameters, Long id) throws ServiceException;

    void delete(Long id) throws ServiceException;

    Integer countAllEntities() throws ServiceException;
}
