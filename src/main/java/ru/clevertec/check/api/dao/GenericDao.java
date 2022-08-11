package ru.clevertec.check.api.dao;

import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.AEntity;

public interface GenericDao<T extends AEntity> {

    T save(T entity);

    T getById(Long id);

    CustomList<T> getAll();

    T update(T entity, Long id);

    void delete(Long id);
}
