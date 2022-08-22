package ru.clevertec.check.api.service;

import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.AEntity;

import java.util.Map;

public interface GenericService<T extends AEntity> {

    T save(Map<String, String> parameters);

    T getById(Long id);

    CustomList<T> getAll();

    CustomList<T> findAll(String pageSizeStr, String pageStr);

    T update(Map<String, String> parameters, Long id);

    void delete(Long id);

    public Integer countAllEntities();
}
