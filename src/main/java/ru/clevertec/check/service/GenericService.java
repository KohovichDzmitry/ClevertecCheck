package ru.clevertec.check.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.check.exceptions.ServiceException;

import java.util.List;

public interface GenericService<T, D> {

    D save(T entity) throws ServiceException;

    D findById(Long id) throws ServiceException;

    List<D> findAll(Pageable pageable);

    D update(T entity, Long id) throws ServiceException;

    void delete(Long id) throws ServiceException;

    Integer countAllEntities();
}
