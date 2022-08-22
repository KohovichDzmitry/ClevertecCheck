package ru.clevertec.check.api.dao;

import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.AEntity;

import java.util.Comparator;

public interface GenericDao<T extends AEntity> {

    T save(T entity);

    T getById(Long id);

    CustomList<T> getAll();

    CustomList<T> findAll(Integer pageSize, Integer page);

    T update(T entity, Long id);

    void delete(Long id);

    CustomList<T> getAllSorted(Comparator<T> comparator);
}
